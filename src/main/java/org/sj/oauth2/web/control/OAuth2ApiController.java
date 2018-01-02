package org.sj.oauth2.web.control;

import org.sj.oauth2.exception.OAuth2Exception;
import org.sj.oauth2.model.Code;
import org.sj.oauth2.model.OpenUserInfo;
import org.sj.oauth2.model.Token;
import org.sj.oauth2.model.User;
import org.sj.oauth2.service.tx.IOAuth2Service;
import org.sj.oauth2.util.Constants;
import org.sj.oauth2.util.OAuth2Request;
import org.sj.oauth2.util.OAuth2Request.AccessTokenRequest;
import org.sj.oauth2.util.OAuth2Request.AuthorizationRequest;
import org.sj.oauth2.util.OAuth2Request.RefreshTokenRequest;
import org.sj.oauth2.util.OAuth2Request.UserInfoRequest;
import org.sj.oauth2.util.OAuth2Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.function.Supplier;

/**
 * OAuth2API控制
 *
 * @author shijian
 * @email shijianws@163.com
 * @date 2017-12-30
 */
@Controller
@RequestMapping("/oauth2")
public class OAuth2ApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ApiController.class);

    @Autowired
    private IOAuth2Service oauth2Service;

    private static User getUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object obj = session.getAttribute(Constants.LOGIN_USER);
            if (obj instanceof User) {
                return (User) obj;
            }
        }
        LOGGER.warn("未找到登录用户!");
        return null;
    }

    private static void exec(HttpServletRequest request, HttpServletResponse response, Supplier<OAuth2Response> func) {
        OAuth2Response resp;
        try {
            resp = func.get(); // 调用处理函数获取响应
            String accept = request.getHeader("Accept");
            if (accept != null && accept.indexOf("json") != -1) {
                LOGGER.debug("响应数据将被转换JSON格式!");
                resp.json(); // JSON格式
            }
        } catch (Exception e) {
            LOGGER.debug("处理请求失败: {}", e.getMessage());
            OAuth2Exception ex;
            if (e instanceof OAuth2Exception) {
                ex = (OAuth2Exception) e;
            } else {
                // 非OAuth2Exception异常, 转换
                ex = new OAuth2Exception(e.getMessage(), Constants.ERROR_STATUS_SERVER_ERROR, Constants.ERROR_SERVER_ERROR);
            }
            resp = OAuth2Response.error(response, ex);
        }
        if (Boolean.TRUE.equals(request.getAttribute(Constants.REDIRECT_URI))) {
            String redirectUri = request.getParameter(Constants.REDIRECT_URI);
            if (redirectUri != null) {
                LOGGER.debug("响应将被重定向到{}", redirectUri);
                resp.redirectUri(redirectUri);
            }
        }
        resp.response(); // 响应
    }

    // http://127.0.0.1:8080/oauth2/authorization?client_id=test&redirect_uri=http%3a%2f%2f127.0.0.1&response_type=code&scope=scope&state=xxoo

    /**
     * 获取授权码
     */
    @RequestMapping(value = "/authorization", method = {RequestMethod.GET, RequestMethod.POST})
    public void authorization(HttpServletRequest request, HttpServletResponse response) {
        exec(request, response, () -> {
            request.setAttribute(Constants.REDIRECT_URI, true); // 允许重定向标识
            AuthorizationRequest req = OAuth2Request.authorizationRequest(request); // 根据请求生成授权码请求对象
            User user = getUser(request); // 获取登录用户
            Code code = oauth2Service.authorization(req, user); // 生成授权码
            return OAuth2Response.authorization(req, response, code);
        });
    }

    // http://127.0.0.1:8080/oauth2/access_token?client_id=test&client_secret=test&grant_type=authorization_code&code=

    /**
     * 获取访问令牌
     */
    @PostMapping(value = "/access_token")
    public void accessToken(HttpServletRequest request, HttpServletResponse response) {
        exec(request, response, () -> {
            AccessTokenRequest req = OAuth2Request.accessTokenRequest(request);
            Token token = oauth2Service.accessToken(req);// 生成访问令牌
            return OAuth2Response.token(response, token);
        });
    }

    // http://127.0.0.1:8080/oauth2/refresh_token?client_id=test&client_secret=test&grant_type=refresh_token&refresh_token=

    /**
     * 刷新访问令牌
     */
    @PostMapping(value = "/refresh_token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        exec(request, response, () -> {
            RefreshTokenRequest req = OAuth2Request.refreshTokenRequest(request); // 根据请求生成刷新访问令牌请求对象
            Token token = oauth2Service.refreshToken(req);// 刷新后的访问令牌
            return OAuth2Response.token(response, token);
        });
    }

    // http://127.0.0.1:8080/oauth2/user_info?access_token=

    /**
     * 获取用户信息
     */
    @PostMapping(value = "/user_info")
    public void userInfo(HttpServletRequest request, HttpServletResponse response) {
        exec(request, response, () -> {
            UserInfoRequest req = OAuth2Request.userInfoRequest(request); // 根据请求生成用户信息请求对象
            OpenUserInfo userInfo = oauth2Service.getUserInfo(req.getAccessToken());
            return OAuth2Response.userInfo(response, userInfo);
        });
    }
}
