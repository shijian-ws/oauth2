package org.sj.oauth2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 访问令牌实体模型
 *
 * @author shijian
 * @email shijianws@163.com
 * @date 2017-12-30
 */
public class Token implements Expired {
    private String id;
    private String tokenType; // 令牌类型
    private String accessToken; // 访问令牌
    private String refreshToken; // 刷新令牌
    private String code; // 生成令牌的授权码
    private String scope; // 作用域
    private String openId; // 令牌所属用户与客户
    private String clientId; // 所属客户
    private Long expiresIn; // 有效时间, 单位: 秒, -1:永久
    private Long issuedAt; // 授权时间

    public Token() {
    }

    public Token(String id, String tokenType, String accessToken, String refreshToken, String code, String scope, String openId, String clientId, Long expiresIn, Long issuedAt) {
        this.id = id;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.code = code;
        this.scope = scope;
        this.openId = openId;
        this.clientId = clientId;
        this.expiresIn = expiresIn;
        this.issuedAt = issuedAt;
    }

    @JsonIgnore
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @JsonIgnore
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @JsonIgnore
    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @JsonIgnore
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    @JsonIgnore
    @Override
    public Long getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Long issuedAt) {
        this.issuedAt = issuedAt;
    }
}
