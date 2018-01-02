package org.sj.oauth2.model;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 超时接口
 *
 * @author shijian
 * @email shijianws@163.com
 * @date 2017-12-30
 */
public interface Expired {
    /**
     * 有效时间, 单位: 秒, -1: 永不超时
     */
    Long getExpiresIn();

    /**
     * 签发时间, 毫秒时间戳
     */
    Long getIssuedAt();

    /**
     * 检查是否超时
     */
    default boolean checkExpired() {
        Long expiresIn = getExpiresIn();
        Objects.requireNonNull(expiresIn, "有效时间不能为空");
        if (expiresIn == -1) {
            return false;
        }
        if (expiresIn == 0) {
            return true;
        }
        Long issuedAt = getIssuedAt();
        Objects.requireNonNull(issuedAt, "签发时间不能为空");
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - issuedAt) > expiresIn;
    }
}
