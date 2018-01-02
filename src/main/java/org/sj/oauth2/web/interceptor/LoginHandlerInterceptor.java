package org.sj.oauth2.web.interceptor;

import org.sj.oauth2.exception.OAuth2Exception;
import org.sj.oauth2.model.User;
import org.sj.oauth2.service.tx.IOAuth2Service;
import org.sj.oauth2.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Base64;

/**
 * 登录处理拦截器
 *
 * @author shijian
 * @email shijianws@163.com
 * @date 2017-12-30
 */
@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginHandlerInterceptor.class);

    @Autowired
    private IOAuth2Service oauth2Service;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!request.getRequestURI().startsWith("/oauth2/authorization")) {
            // 非授权码获取路径不需要用户登录
            return true;
        }
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Constants.LOGIN_USER);
        if (user != null) {
            // 已登录
            return true;
        }
        // Authorization: Basic YWRtaW46MTIzNDU2
        String authorization = request.getHeader(Constants.HTTP_HEADER_AUTHORIZATION);
        String message = "请登录";
        if (authorization != null && authorization.startsWith("Basic ")) {
            int pos = authorization.indexOf(' ');
            try {
                String info = new String(Base64.getDecoder().decode(authorization.substring(pos + 1))); // 截取, Base64解码
                pos = info.indexOf(':'); // 用户名密码分割符
                if (pos != -1) {
                    String username = info.substring(0, pos); // 用户名
                    String password = info.substring(pos + 1); // 密码
                    if (username.isEmpty() || password.isEmpty()) {
                        message = "用户名或密码不能为空";
                    } else {
                        user = oauth2Service.login(username, password); // 登录用户
                        if (user != null) {
                            session.setAttribute(Constants.LOGIN_USER, user); // 存储到中会话
                            return true; // 放行
                        }
                        message = "用户名或密码不正确";
                    }
                }
            } catch (Exception e) {
                LOGGER.error("解析Authorization失败: {}", e.getMessage());
            }
        }
        // 未登录
        throw new OAuth2Exception(message, Constants.ERROR_STATUS_INVALID_AUTHORIZATION, Constants.ERROR_ACCESS_DENIED, Constants.ERRCODE_INVALID_USER_CREDENTIAL);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
