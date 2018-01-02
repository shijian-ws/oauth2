package org.sj.oauth2.service.tx.impl;

import org.sj.oauth2.dao.IOAuth2DAO;
import org.sj.oauth2.exception.OAuth2Exception;
import org.sj.oauth2.model.*;
import org.sj.oauth2.service.tx.IOAuth2Service;
import org.sj.oauth2.util.Constants;
import org.sj.oauth2.util.OAuth2Request.AccessTokenRequest;
import org.sj.oauth2.util.OAuth2Request.AuthorizationRequest;
import org.sj.oauth2.util.OAuth2Request.RefreshTokenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * OAuth2业务操作接口实现类
 *
 * @author shijian
 * @email shijianws@163.com
 * @date 2017-12-30
 */
@Service
public class OAuth2ServiceImpl implements IOAuth2Service {
    @Autowired
    private IOAuth2DAO oauth2DAO;

    @Override
    public User login(String username, String password) {
        return oauth2DAO.getUserByUsernameAndPassword(username, password);
    }

    /**
     * 创建uuid
     *
     * @return
     */
    private static String createUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 创建字符串
     */
    private static String createString() {
        return new StringBuilder().append(createUUID()).insert(16, ThreadLocalRandom.current().nextInt(100000)).toString();
    }

    /**
     * 获取客户信息
     */
    private static ClientInfo getClientInfo(IOAuth2DAO oauth2DAO, String clientId, String clientSecret) {
        ClientInfo clientInfo;
        if (clientSecret == null) {
            clientInfo = oauth2DAO.getClientInfoByClientId(clientId);
        } else {
            clientInfo = oauth2DAO.getClientInfoByClientIdAndClientSecret(clientId, clientSecret);
        }
        if (clientInfo == null) {
            throw new OAuth2Exception("客户凭证认证失败", Constants.ERROR_STATUS_INVALID_PARAMETER, Constants.ERROR_INVALID_CLIENT, Constants.ERRCODE_INVALID_CLIENT_CREDENTIAL);
        }
        return clientInfo;
    }

    /**
     * 校验客户凭证中的重定向URI与请求中的重定向URI
     *
     * @param clientRedirectUri
     * @param reqRedirectUri
     */
    private static void checkRedirectUri(String clientRedirectUri, String reqRedirectUri) {
        if (!reqRedirectUri.toLowerCase().startsWith(clientRedirectUri.toLowerCase())) {
            throw new OAuth2Exception("重定向地址与客户凭证绑定重定向地址匹配", Constants.ERROR_STATUS_INVALID_PARAMETER, Constants.ERROR_INVALID_REQUEST, Constants.ERRCODE_REDIRECT_URI_NOT_MATCH);
        }
    }

    @Override
    public Code authorization(AuthorizationRequest request, User login) {
        String clientId = request.getClientId();
        ClientInfo clientInfo = getClientInfo(oauth2DAO, clientId, null); // 查找客户信息
        String redirectUri = request.getRedirectUri();
        checkRedirectUri(clientInfo.getRedirectUri(), redirectUri); // 校验重定向URI
        String userId = login.getId();
        OpenInfo openInfo = oauth2DAO.getOpenInfoByClientIdAndUserId(clientId, userId);
        if (openInfo == null) {
            // 未找到用户与客户关联, 创建关联关系
            openInfo = new OpenInfo(createUUID(), userId, clientId, System.currentTimeMillis());
            oauth2DAO.saveOpenInfo(openInfo);
        }
        Code code = new Code(createUUID(), createString(), openInfo.getOpenId(), clientId, request.getScope(), redirectUri, false, clientInfo.getExpiresIn(), System.currentTimeMillis());
        oauth2DAO.saveCode(code);
        return code;
    }

    @Override
    public Token accessToken(AccessTokenRequest req) {
        String clientId = req.getClientId();
        ClientInfo clientInfo = getClientInfo(oauth2DAO, clientId, req.getClientSecret()); // 查找客户信息
        String code = req.getCode();
        Code oauthCode = oauth2DAO.getCodeByCode(code);
        if (oauthCode == null) {
            throw new OAuth2Exception("授权码错误", Constants.ERROR_STATUS_INVALID_PARAMETER, Constants.ERROR_INVALID_REQUEST, Constants.ERRCODE_INVALID_CODE);
        }
        String redirectUri = req.getRedirectUri();
        if (redirectUri != null && !redirectUri.equals(oauthCode.getRedirectUri())) {
            throw new OAuth2Exception("生成授权码重定向地址与当前重定向地址不一致", Constants.ERROR_STATUS_INVALID_PARAMETER, Constants.ERROR_INVALID_REQUEST, Constants.ERRCODE_REDIRECT_URI_NOT_MATCH);
        }
        if (oauthCode.getUseFlag()) {
            // TODO 多次被使用是否代表已被泄露?
            throw new OAuth2Exception("授权码被使用", Constants.ERROR_STATUS_INVALID_PARAMETER, Constants.ERROR_INVALID_REQUEST, Constants.ERRCODE_CODE_USED);
        }
        if (oauthCode.checkExpired()) {
            throw new OAuth2Exception("授权码超时", Constants.ERROR_STATUS_INVALID_PARAMETER, Constants.ERROR_INVALID_REQUEST, Constants.ERRCODE_CODE_EXPIRED);
        }
        if (!clientId.equals(oauthCode.getClientId())) {
            throw new OAuth2Exception("授权码非当前客户所有", Constants.ERROR_STATUS_INVALID_PARAMETER, Constants.ERROR_INVALID_REQUEST, Constants.ERRCODE_CLIENT_NOT_MATCH);
        }
        oauth2DAO.updateCodeAsUse(code); // 更新授权码被使用状态
        Token token = new Token(createUUID(), Constants.TOKEN_TYPE_BEARER, createString(), createString(), code, req.getScope(), oauthCode.getOpenId(), clientId, clientInfo.getExpiresIn(), System.currentTimeMillis());
        oauth2DAO.saveToken(token); // 保存访问令牌
        return token;
    }

    @Override
    public Token refreshToken(RefreshTokenRequest req) {
        String clientId = req.getClientId();
        ClientInfo clientInfo = getClientInfo(oauth2DAO, clientId, req.getClientSecret()); // 查找客户信息
        String refreshToken = req.getRefreshToken();
        Token token = oauth2DAO.getTokenByRefreshToken(refreshToken);
        if (token == null) {
            throw new OAuth2Exception("刷新令牌错误", Constants.ERROR_STATUS_INVALID_PARAMETER, Constants.ERROR_INVALID_REQUEST, Constants.ERRCODE_INVALID_REFRESH_TOKEN);
        }
        // TODO 刷新令牌是否有超时?
        if (!clientId.equals(token.getClientId())) {
            throw new OAuth2Exception("刷新令牌非当前客户所有", Constants.ERROR_STATUS_INVALID_PARAMETER, Constants.ERROR_INVALID_REQUEST, Constants.ERRCODE_CLIENT_NOT_MATCH);
        }
        Long expiresIn = token.getExpiresIn();
        if (expiresIn > -1) {
            oauth2DAO.refreshToken(refreshToken, System.currentTimeMillis()); // 刷新签发时间
        }
        return token;
    }

    @Override
    public OpenUserInfo getUserInfo(String accessToken) {
        Token token = oauth2DAO.getTokenByAccessToken(accessToken);
        if (token == null) {
            throw new OAuth2Exception("访问令牌错误", Constants.ERROR_STATUS_INVALID_PARAMETER, Constants.ERROR_INVALID_REQUEST, Constants.ERRCODE_INVALID_ACCESS_TOKEN);
        }
        if (token.checkExpired()) {
            throw new OAuth2Exception("访问令牌超时", Constants.ERROR_STATUS_INVALID_PARAMETER, Constants.ERROR_INVALID_REQUEST, Constants.ERRCODE_ACCESS_TOKEN_EXPIRED);
        }
        return oauth2DAO.getUserInfoByOpenId(token.getOpenId());
    }

    @Override
    public void saveClientInfo(ClientInfo info) {
        if (oauth2DAO.checkClientId(info.getClientId())) {
            throw new OAuth2Exception("客户凭证ID已存在", Constants.ERROR_STATUS_INVALID_PARAMETER, Constants.ERROR_INVALID_CLIENT, Constants.ERRCODE_INVALID_CLIENT_CREDENTIAL);
        }
        info.setId(createUUID());
        info.setCreationTime(System.currentTimeMillis());
        oauth2DAO.saveClientInfo(info);
    }

    @Override
    public void saveUser(User user) {
        if (oauth2DAO.checkUsername(user.getUsername())) {
            throw new OAuth2Exception("用户名已存在", Constants.ERROR_STATUS_INVALID_PARAMETER, Constants.ERROR_INVALID_REQUEST, Constants.ERRCODE_INVALID_USER_CREDENTIAL);
        }
        user.setId(createUUID());
        user.setCreationTime(System.currentTimeMillis());
        oauth2DAO.saveUser(user);
    }
}
