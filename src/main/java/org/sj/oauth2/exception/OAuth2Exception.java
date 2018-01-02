package org.sj.oauth2.exception;

import java.util.Objects;

/**
 * OAuth2异常类
 *
 * @author shijian
 * @email shijianws@163.com
 * @date 2017-12-30
 */
public class OAuth2Exception extends RuntimeException {
    private int status;
    private String error;
    private Integer errcode;

    public OAuth2Exception(String message, int status, String error) {
        super(message);
        Objects.requireNonNull(error);
        this.status = status;
        this.error = error;
    }

    public OAuth2Exception(String message, int status, String error, int errcode) {
        super(message);
        Objects.requireNonNull(error);
        this.status = status;
        this.error = error;
        this.errcode = errcode;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public Integer getErrcode() {
        return errcode;
    }
}
