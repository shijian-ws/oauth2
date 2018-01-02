package org.sj.oauth2.util;

import java.nio.charset.Charset;

/**
 * 常量类
 *
 * @author shijian
 * @email shijianws@163.com
 * @date 2017-12-30
 */
public interface Constants {
    /**
     * 操作类型
     */
    String RESPONSE_TYPE = "response_type";
    /**
     * 授权码
     */
    String RESPONSE_TYPE_CODE = "code";
    /**
     * 客户凭证ID
     */
    String CLIENT_ID = "client_id";
    /**
     * 重定向URI
     */
    String REDIRECT_URI = "redirect_uri";
    /**
     * 资源作用域
     */
    String SCOPE = "scope";
    /**
     * 请求响应状态
     */
    String STATE = "state";
    /**
     * 授权类型
     */
    String GRANT_TYPE = "grant_type";
    /**
     * 授权码类型
     */
    String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
    /**
     * 客户凭证私钥
     */
    String CLIENT_SECRET = "client_secret";
    /**
     * 令牌类型
     */
    String TOKEN_TYPE = "token_type";
    /**
     * Bearer 令牌类型
     */
    String TOKEN_TYPE_BEARER = "bearer";
    /**
     * 访问令牌
     */
    String ACCESS_TOKEN = "access_token";
    /**
     * 刷新令牌
     */
    String REFRESH_TOKEN = "refresh_token";
    /**
     * 有效时间, 单位: 秒
     */
    String EXPIRES_IN = "expires_in";
    /**
     * 用户所属客户唯一ID
     */
    String OPEN_ID = "open_id";
    /**
     * 错误
     */
    String ERROR = "error";
    /**
     * 错误额外描述
     */
    String ERROR_DESCRIPTION = "error_description";
    /**
     * 请求缺少必需的参数、包含无效的参数值、包含一个参数超过一次或其他不良格式
     */
    String ERROR_INVALID_REQUEST = "invalid_request";
    /**
     * 非法响应操作类型
     */
    String ERROR_INVALID_RESPONSE_TYPE = "invalid_response_type";
    /**
     * 客户端身份验证失败,
     * 授权服务器可以返回HTTP 401（未授权）状态码来指出支持的HTTP身份验证方案。
     * 如果客户端试图通过“Authorization”请求标头域进行身份验证，授权服务器必须响应HTTP 401（未授权）状态码，并包含与客户端使用的身份验证方案匹配的“WWW-Authenticate”响应标头字段
     */
    String ERROR_INVALID_CLIENT = "invalid_client";
    /**
     * 非法授权
     */
    String ERROR_INVALID_GRANT = "invalid_grant";
    /**
     * 资源所有者或授权服务器拒绝该请求
     */
    String ERROR_ACCESS_DENIED = "access_denied";
    /**
     * 请求的范围无效，未知的或格式不正确
     */
    String ERROR_INVALID_SCOPE = "invalid_scope";
    /**
     * 授权服务器遇到意外情况导致其无法执行该请求
     */
    String ERROR_SERVER_ERROR = "server_error";
    /**
     * 授权服务器由于暂时超载或服务器维护目前无法处理请求
     */
    String ERROR_TEMPORARILY_UNAVAILABLE = "temporarily_unavailable";
    /**
     * 错误状态
     */
    String ERROR_STATUS = "status";
    /**
     * 请求方式不被支持
     */
    int ERROR_STATUS_UNSUPPORTED_METHOD = 405;
    /**
     * 非法的请求参数
     */
    int ERROR_STATUS_INVALID_PARAMETER = 412;
    /**
     * 请求体类型不被支持
     */
    int ERROR_STATUS_UNSUPPORTED_MEDIA_TYPE = 415;
    /**
     * 非法用户凭证
     */
    int ERROR_STATUS_INVALID_AUTHORIZATION = 401;
    /**
     * 服务异常
     */
    int ERROR_STATUS_SERVER_ERROR = 500;

    /**
     * 登录用户对象在会话中的属性KEY
     */
    String LOGIN_USER = "LOGIN_USER";

    /**
     * 用户认证信息请求头
     */
    String HTTP_HEADER_AUTHORIZATION = "Authorization";
    /**
     * 需要用户认证请求头
     */
    String HTTP_HEADER_WWW_AUTHENTICATE = "WWW-Authenticate";

    /**
     * 业务错误状态
     */
    String ERRCODE = "errcode";

    /**
     * 业务错误状态码
     */
    /**
     * 非法用户凭证, 用户名或密码错误
     */
    int ERRCODE_INVALID_USER_CREDENTIAL = 40001;
    /**
     * 非法授权类型, 非code值
     */
    int ERRCODE_INVALID_RESPONSE_TYPE = 40002;
    /**
     * 非法客户凭证, clientid与client_secret不正确
     */
    int ERRCODE_INVALID_CLIENT_CREDENTIAL = 40003;
    /**
     * 重定向地址不合法
     */
    int ERRCODE_INVALID_REDIRECT_URI = 40004;
    /**
     * 请求权限超出生成code或token时的权限
     */
    int ERRCODE_INVALID_SCOPE = 40005;
    /**
     * 非法授权类型, 非authorization_code值
     */
    int ERRCODE_INVALID_GRANT_TYPE = 40006;
    /**
     * 非法授权码, code值找不到对应实体
     */
    int ERRCODE_INVALID_CODE = 40007;
    /**
     * 请求头的token类型错误, 非bearer值
     */
    int ERRCODE_INVALID_TOKEN_TYPE = 40008;
    /**
     * 获取用户信息的访问令牌找不到对应实体
     */
    int ERRCODE_INVALID_ACCESS_TOKEN = 40009;
    /**
     * 刷新访问令牌时的刷新令牌找不到对应实体
     */
    int ERRCODE_INVALID_REFRESH_TOKEN = 40010;

    /**
     * 授权码被使用
     */
    int ERRCODE_CODE_USED = 41001;
    /**
     * 授权码超时
     */
    int ERRCODE_CODE_EXPIRED = 41002;
    /**
     * 访问令牌超时
     */
    int ERRCODE_ACCESS_TOKEN_EXPIRED = 41003;
    /**
     * 刷新令牌超时
     */
    int ERRCODE_REFRESH_TOKEN_EXPIRED = 41004;
    /**
     * clientid与生成code的clientid不匹配
     */
    int ERRCODE_CLIENT_NOT_MATCH = 41005;
    /**
     * 重定向地址与客户信息绑定的重定向地址不匹配
     */
    int ERRCODE_REDIRECT_URI_NOT_MATCH = 41006;

    Charset CHARSET_UTF_8 = Charset.forName("UTF-8");
}
