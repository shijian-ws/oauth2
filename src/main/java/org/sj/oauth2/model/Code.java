package org.sj.oauth2.model;

/**
 * 授权码实体模型
 *
 * @author shijian
 * @email shijianws@163.com
 * @date 2017-12-30
 */
public class Code implements Expired {
    private String id;
    private String code; // 授权码
    private String openId; // 所属用户与客户
    private String clientId; // 所属客户
    private String scope; // 作用域
    private String redirectUri; //重定向URI
    private Boolean useFlag; // 是否被使用
    private Long expiresIn; // 有效时间, 单位: 秒, -1:永久
    private Long issuedAt; // 授权时间

    public Code() {
    }

    public Code(String id, String code, String openId, String clientId, String scope, String redirectUri, Boolean useFlag, Long expiresIn, Long issuedAt) {
        this.id = id;
        this.code = code;
        this.openId = openId;
        this.clientId = clientId;
        this.scope = scope;
        this.redirectUri = redirectUri;
        this.useFlag = useFlag;
        this.expiresIn = expiresIn;
        this.issuedAt = issuedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public Boolean getUseFlag() {
        return useFlag;
    }

    public void setUseFlag(Boolean useFlag) {
        this.useFlag = useFlag;
    }

    @Override
    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public Long getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Long issuedAt) {
        this.issuedAt = issuedAt;
    }
}
