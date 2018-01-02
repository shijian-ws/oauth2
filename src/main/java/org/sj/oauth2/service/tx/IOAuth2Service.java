package org.sj.oauth2.service.tx;

import org.sj.oauth2.model.*;
import org.sj.oauth2.util.OAuth2Request.AccessTokenRequest;
import org.sj.oauth2.util.OAuth2Request.AuthorizationRequest;
import org.sj.oauth2.util.OAuth2Request.RefreshTokenRequest;

/**
 * OAuth2业务操作接口
 *
 * @author shijian
 * @email shijianws@163.com
 * @date 2017-12-30
 */
public interface IOAuth2Service {
    /**
     * 用户登录, 如果未找到则返回空
     */
    User login(String username, String password);

    /**
     * 申请授权码
     *
     * @param request 请求信息
     * @param login   登录用户
     * @return
     */
    Code authorization(AuthorizationRequest request, User login);

    /**
     * 申请访问令牌
     *
     * @param req 请求信息
     * @return
     */
    Token accessToken(AccessTokenRequest req);

    /**
     * 刷新访问令牌
     *
     * @param req 请求信息
     * @return
     */
    Token refreshToken(RefreshTokenRequest req);

    /**
     * 获取开放用户信息
     */
    OpenUserInfo getUserInfo(String accessToken);

    /**
     * 创建客户凭证信息
     */
    void saveClientInfo(ClientInfo info);

    /**
     * 创建用户信息
     */
    void saveUser(User user);
}
