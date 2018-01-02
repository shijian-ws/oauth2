package org.sj.oauth2.util;

import org.sj.oauth2.exception.OAuth2Exception;
import org.sj.oauth2.model.Code;
import org.sj.oauth2.model.OpenUserInfo;
import org.sj.oauth2.model.Token;
import org.sj.oauth2.util.OAuth2Request.AuthorizationRequest;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * OAuth2响应工具类
 *
 * @author shijian
 * @email shijianws@163.com
 * @date 2017-12-30
 */
public final class OAuth2Response {
    private String contentType;
    private boolean isJson;
    private String redirectUri;
    private final HttpServletResponse response;
    private final Map<String, Object> data;

    public OAuth2Response(HttpServletResponse response, Map<String, Object> data) {
        this.response = response;
        this.data = data;
    }

    /**
     * 响应
     */
    public void response() {
        StringBuilder buffer = new StringBuilder();
        Integer status = null;
        if (data != null && !data.isEmpty()) {
            Object obj = data.remove(Constants.ERROR_STATUS);
            if (obj instanceof Integer) {
                status = (Integer) obj;
            }
        }
        if (isJson) {
            // application/json格式
            buffer.append("{");
            if (data != null && !data.isEmpty()) {
                for (Entry<String, Object> entry : data.entrySet()) {
                    Object value = entry.getValue();
                    if (value == null) {
                        continue;
                    }
                    buffer.append('"').append(entry.getKey()).append("\":");
                    if (value instanceof Number || value instanceof Boolean) {
                        buffer.append(value.toString());
                    } else {
                        buffer.append('"').append(value.toString()).append('"');
                    }
                    buffer.append(',');
                }
                buffer.deleteCharAt(buffer.length() - 1);
            }
            buffer.append("}");
        } else {
            // text/html格式
            if (data != null && !data.isEmpty()) {
                for (Entry<String, Object> entry : data.entrySet()) {
                    Object val = entry.getValue();
                    if (val == null) {
                        continue;
                    }
                    if (redirectUri != null) {
                        try {
                            val = URLEncoder.encode(entry.getValue().toString(), Constants.CHARSET_UTF_8.name()); // URL编码
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    buffer.append(entry.getKey()).append('=').append(val).append('&');
                }
                buffer.deleteCharAt(buffer.length() - 1);
            }
        }
        try {
            if (contentType != null) {
                // 设置响应消息体类型
                response.setContentType(contentType);
            }
            if (redirectUri != null) {
                // 重定向拼接url
                if (redirectUri.indexOf('?') == -1) {
                    buffer.insert(0, '?');
                } else {
                    buffer.insert(0, '&');
                }
                buffer.insert(0, redirectUri);
                response.sendRedirect(buffer.toString()); // 触发重定向
                return;
            }
            if (status != null) {
                response.setStatus(status); // 设置响应状态码
            }
            response.getWriter().append(buffer); // 响应数据
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * json格式
     */
    public OAuth2Response json() {
        this.contentType = "application/json";
        this.isJson = true;
        return this;
    }

    /**
     * json格式
     */
    public OAuth2Response redirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    /**
     * 授权码响应
     */
    public static OAuth2Response authorization(AuthorizationRequest request, HttpServletResponse response, Code code) {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.RESPONSE_TYPE_CODE, code.getCode());
        map.put(Constants.STATE, request.getState());
        return new OAuth2Response(response, map).redirectUri(request.getRedirectUri());
    }

    /**
     * 访问令牌响应
     */
    public static OAuth2Response token(HttpServletResponse response, Token token) {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TOKEN_TYPE, Constants.TOKEN_TYPE_BEARER);
        map.put(Constants.ACCESS_TOKEN, token.getAccessToken());
        map.put(Constants.REFRESH_TOKEN, token.getRefreshToken());
        map.put(Constants.EXPIRES_IN, token.getExpiresIn());
        map.put(Constants.OPEN_ID, token.getOpenId());
        return new OAuth2Response(response, map);
    }

    /**
     * 用户信息响应
     */
    public static OAuth2Response userInfo(HttpServletResponse response, OpenUserInfo userInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.OPEN_ID, userInfo.getOpenId());
        map.put("nickname", userInfo.getNickname());
        map.put("head_img_url", userInfo.getHeadImgUrl());
        map.put("sex", userInfo.getSex());
        map.put("country", userInfo.getCountry());
        map.put("province", userInfo.getProvince());
        map.put("city", userInfo.getCity());
        return new OAuth2Response(response, map);
    }

    /**
     * 错误响应
     */
    public static OAuth2Response error(HttpServletResponse response, OAuth2Exception ex) {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.ERROR, ex.getError());
        map.put(Constants.ERROR_DESCRIPTION, ex.getMessage());
        map.put(Constants.ERROR_STATUS, ex.getStatus());
        Integer errcode = ex.getErrcode();
        if (errcode != null) {
            map.put(Constants.ERRCODE, errcode);
        }
        return new OAuth2Response(response, map);
    }
}
