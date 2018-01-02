package org.sj.oauth2.model;

/**
 * 开放用户信息
 *
 * @author shijian
 * @email shijianws@163.com
 * @date 2017-12-30
 */
public class OpenUserInfo {
    private String openId;
    private String nickname; // 昵称
    private String headImgUrl; // 用户头像
    private Integer sex; // 用户性别
    private String country; // 国家
    private String province; // 省份
    private String city; // 城市

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
