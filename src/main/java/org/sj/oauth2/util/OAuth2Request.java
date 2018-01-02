package org.sj.oauth2.util;

import org.sj.oauth2.exception.OAuth2Exception;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * OAuth2请求工具类
 *
 * @author shijian
 * @email shijianws@163.com
 * @date 2017-12-30
 */
public abstract class OAuth2Request {
    protected final String clientId;
    protected final String redirectUri;
    protected final String scope;

    protected HttpServletRequest request;

    protected OAuth2Request(HttpServletRequest request) {
        Objects.requireNonNull(request, "请求对象不能为空!");
        clientId = request.getParameter(Constants.CLIENT_ID);
        redirectUri = request.getParameter(Constants.REDIRECT_URI);
        scope = request.getParameter(Constants.SCOPE);
        this.request = request;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getScope() {
        return scope;
    }

    public String getParameter(String name) {
        return request.getParameter(name);
    }

    private Set<String> parameters = new HashSet<>();

    /**
     * 开始校验参数
     */
    protected final void validate() {
        Set<String> set = new HashSet<>();
        for (String name : parameters) {
            String value = request.getParameter(name);
            if (value == null || value.isEmpty()) {
                set.add(name);
            }
        }
        if (!set.isEmpty()) {
            throw new OAuth2Exception(String.format("缺少%s参数值", set), Constants.ERROR_STATUS_INVALID_PARAMETER, Constants.ERROR_INVALID_REQUEST);
        }
    }

    /**
     * 设置校验参数
     */
    protected final void initValidator(String... parameters) {
        if (parameters != null && parameters.length > 0) {
            for (String name : parameters) {
                this.parameters.add(name);
            }
        }
    }

    /**
     * 授权码请求对象
     */
    public static AuthorizationRequest authorizationRequest(HttpServletRequest request) {
        return new AuthorizationRequest(request);
    }

    /**
     * 授权码请求
     */
    public static class AuthorizationRequest extends OAuth2Request {
        private final String responseType;
        private final String state;

        protected AuthorizationRequest(HttpServletRequest request) {
            super(request);
            super.initValidator(Constants.CLIENT_ID, Constants.REDIRECT_URI, Constants.RESPONSE_TYPE, Constants.STATE);
            super.validate();
            responseType = request.getParameter(Constants.RESPONSE_TYPE);
            if (!Constants.RESPONSE_TYPE_CODE.equals(responseType)) {
                throw new OAuth2Exception(String.format("%s参数值必须是%s不能为%s", Constants.RESPONSE_TYPE, Constants.RESPONSE_TYPE_CODE, responseType), Constants.ERROR_STATUS_INVALID_PARAMETER, Constants.ERROR_INVALID_RESPONSE_TYPE, Constants.ERRCODE_INVALID_RESPONSE_TYPE);
            }
            state = request.getParameter(Constants.STATE);
        }

        public String getResponseType() {
            return responseType;
        }

        public String getState() {
            return state;
        }
    }

    /**
     * 获取访问令牌请求
     */
    public static AccessTokenRequest accessTokenRequest(HttpServletRequest request) {
        return new AccessTokenRequest(request);
    }

    /**
     * 访问令牌请求
     */
    public static class AccessTokenRequest extends OAuth2Request {
        private final String clientSecret;
        private final String grantType;
        private final String code;

        protected AccessTokenRequest(HttpServletRequest request) {
            super(request);
            super.initValidator(Constants.CLIENT_ID, Constants.CLIENT_SECRET, Constants.GRANT_TYPE, Constants.RESPONSE_TYPE_CODE);
            super.validate();
            clientSecret = request.getParameter(Constants.CLIENT_SECRET);
            grantType = request.getParameter(Constants.GRANT_TYPE);
            if (!Constants.GRANT_TYPE_AUTHORIZATION_CODE.equals(grantType)) {
                throw new OAuth2Exception(String.format("%s参数值必须是%s不能为%s", Constants.GRANT_TYPE, Constants.GRANT_TYPE_AUTHORIZATION_CODE, grantType), Constants.ERROR_STATUS_INVALID_PARAMETER, Constants.ERROR_INVALID_GRANT, Constants.ERRCODE_INVALID_GRANT_TYPE);
            }
            code = request.getParameter(Constants.RESPONSE_TYPE_CODE);
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public String getGrantType() {
            return grantType;
        }

        public String getCode() {
            return code;
        }
    }

    /**
     * 获取刷新访问令牌请求
     */
    public static RefreshTokenRequest refreshTokenRequest(HttpServletRequest request) {
        return new RefreshTokenRequest(request);
    }

    /**
     * 访问令牌请求
     */
    public static class RefreshTokenRequest extends OAuth2Request {
        private final String clientSecret;
        private final String grantType;
        private final String refreshToken;

        protected RefreshTokenRequest(HttpServletRequest request) {
            super(request);
            super.initValidator(Constants.CLIENT_ID, Constants.CLIENT_SECRET, Constants.GRANT_TYPE, Constants.REFRESH_TOKEN);
            super.validate();
            clientSecret = request.getParameter(Constants.CLIENT_SECRET);
            grantType = request.getParameter(Constants.GRANT_TYPE);
            if (!Constants.REFRESH_TOKEN.equals(grantType)) {
                throw new OAuth2Exception(String.format("%s参数值必须是%s不能为%s", Constants.GRANT_TYPE, Constants.REFRESH_TOKEN, grantType), Constants.ERROR_STATUS_INVALID_PARAMETER, Constants.ERROR_INVALID_RESPONSE_TYPE);
            }
            refreshToken = request.getParameter(Constants.REFRESH_TOKEN);
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public String getGrantType() {
            return grantType;
        }

        public String getRefreshToken() {
            return refreshToken;
        }
    }

    /**
     * 获取用户信息请求
     */
    public static UserInfoRequest userInfoRequest(HttpServletRequest request) {
        return new UserInfoRequest(request);
    }

    public static class UserInfoRequest extends OAuth2Request {
        private String accessToken;

        public UserInfoRequest(HttpServletRequest request) {
            super(request);
            boolean flag = true;
            String authorization = request.getHeader(Constants.HTTP_HEADER_AUTHORIZATION);
            if (authorization != null) {
                if (authorization.toLowerCase().startsWith(Constants.TOKEN_TYPE_BEARER)) {
                    try {
                        accessToken = new String(Base64.getDecoder().decode(authorization.substring(Constants.TOKEN_TYPE_BEARER.length() + 1)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (accessToken == null) {
                        throw new OAuth2Exception("Bearer格式access_token参数错误", Constants.ERROR_STATUS_INVALID_PARAMETER, Constants.ERROR_INVALID_REQUEST);
                    }
                    flag = false;
                }
            }
            if (flag) {
                super.initValidator(Constants.ACCESS_TOKEN);
                super.validate();
                accessToken = request.getParameter(Constants.ACCESS_TOKEN);
            }
        }

        public String getAccessToken() {
            return accessToken;
        }
    }
}
