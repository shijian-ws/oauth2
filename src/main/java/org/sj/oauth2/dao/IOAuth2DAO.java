package org.sj.oauth2.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.sj.oauth2.model.*;

/**
 * OAuth2数据访问接口
 *
 * @author shijian
 * @email shijianws@163.com
 * @date 2017-12-30
 */
@Mapper
public interface IOAuth2DAO {
    /**
     * 根据客户凭证ID查询客户信息
     */
    ClientInfo getClientInfoByClientId(@Param("clientId") String clientId);

    /**
     * 根据客户凭证ID与客户私钥查询客户信息
     */
    ClientInfo getClientInfoByClientIdAndClientSecret(@Param("clientId") String clientId, @Param("clientSecret") String clientSecret);

    /**
     * 检查客户凭证ID是否存在
     */
    boolean checkClientId(@Param("clientId") String clientId);

    /**
     * 保存客户端
     */
    long saveClientInfo(ClientInfo clientInfo);

    /**
     * 更新客户端信息
     */
    long updateClientInfo(ClientInfo clientInfo);

    /**
     * 根据手机号查询用户
     */
    User getUserByMobile(@Param("mobile") String mobile);

    /**
     * 根据手机号和密码查询用户
     */
    User getUserByMobileAndPassword(@Param("mobile") String mobile, @Param("password") String password);

    /**
     * 根据电邮查询用户
     */
    User getUserByEmail(@Param("email") String email);

    /**
     * 根据电邮和密码查询用户
     */
    User getUserByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    /**
     * 根据用户名查询用户
     */
    User getUserByUsername(@Param("username") String username);

    /**
     * 根据用户名和密码查询用户
     */
    User getUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    boolean checkUsername(@Param("username") String username);

    /**
     * 创建用户
     */
    long saveUser(User user);

    /**
     * 更新用户
     */
    long updateUser(User user);

    /**
     * 移除用户手机
     */
    long removeMobile(@Param("id") String userId, @Param("creater") String creater);

    /**
     * 移除用户电邮
     */
    long removeEmail(@Param("id") String userId, @Param("creater") String creater);

    /**
     * 移除用户用户名
     */
    long removeUsername(@Param("id") String userId, @Param("creater") String creater);

    /**
     * 查询客户与用户映射关系
     */
    OpenInfo getOpenInfoById(@Param("openId") String openId);

    /**
     * 查询客户端与用户映射关系
     */
    OpenInfo getOpenInfoByClientIdAndUserId(@Param("clientId") String clientId, @Param("userId") String userId);

    /**
     * 保存客户与用户映射关系
     */
    long saveOpenInfo(OpenInfo openInfo);

    /**
     * 查询授权码信息
     */
    Code getCodeByCode(@Param("code") String code);

    /**
     * 保存授权码
     */
    long saveCode(Code code);

    /**
     * 更新授权码被使用
     */
    long updateCodeAsUse(@Param("code") String code);


    /**
     * 根据请求凭证获取请求凭证信息
     */
    Token getTokenByAccessToken(@Param("accessToken") String accessToken);

    /**
     * 根据刷新凭证获取请求凭证信息
     */
    Token getTokenByRefreshToken(@Param("refreshToken") String refreshToken);

    /**
     * 保存令牌
     */
    long saveToken(Token token);

    /**
     * 刷新访问令牌
     */
    long refreshToken(@Param("refreshToken") String refreshToken, @Param("issuedAt") long issuedAt);

    /**
     * 查询开放用户信息
     */
    OpenUserInfo getUserInfoByOpenId(@Param("openId") String openId);
}
