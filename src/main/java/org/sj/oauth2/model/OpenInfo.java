package org.sj.oauth2.model;

/**
 * 用户与客户映射关系模型
 *
 * @author shijian
 * @email shijianws@163.com
 * @date 2017-12-30
 */
public class OpenInfo {
    private String openId;
    private String userId; // 用户ID
    private String clientId; // 客户ID
    private Long creationTime; // 关联时间

    public OpenInfo() {
    }

    public OpenInfo(String openId, String userId, String clientId, Long creationTime) {
        this.openId = openId;
        this.userId = userId;
        this.clientId = clientId;
        this.creationTime = creationTime;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }
}
