package org.sj.oauth2.web.control;

import org.sj.oauth2.exception.OAuth2Exception;
import org.sj.oauth2.model.ClientInfo;
import org.sj.oauth2.model.User;
import org.sj.oauth2.service.tx.IOAuth2Service;
import org.sj.oauth2.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理控制
 *
 * @author shijian
 * @email shijianws@163.com
 * @date 2017-12-30
 */
@RestController
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private IOAuth2Service oauth2Service;

    /**
     * 创建客户凭证
     */
    @PostMapping("/client")
    public Object saveClientInfo(@RequestBody ClientInfo info) {
        String clientId = info.getClientId();
        if (clientId == null || clientId.isEmpty()) {
            throw new OAuth2Exception("客户ID不能为空", Constants.ERROR_STATUS_INVALID_PARAMETER, Constants.ERROR_INVALID_REQUEST);
        }
        String clientSecret = info.getClientSecret();
        if (clientSecret == null || clientSecret.isEmpty()) {
            throw new OAuth2Exception("客户secret不能为空", Constants.ERROR_STATUS_INVALID_PARAMETER, Constants.ERROR_INVALID_REQUEST);
        }
        String redirectUri = info.getRedirectUri();
        if (redirectUri == null || redirectUri.isEmpty()) {
            throw new OAuth2Exception("客户绑定重定向地址不能为空", Constants.ERROR_STATUS_INVALID_PARAMETER, Constants.ERROR_INVALID_REQUEST);
        }
        String name = info.getName();
        if (name == null) {
            info.setName("客户名称");
        }
        Long expiresIn = info.getExpiresIn();
        if (expiresIn == null) {
            info.setExpiresIn(3600L);
        }
        oauth2Service.saveClientInfo(info);
        return "OK";
    }

    /**
     * 创建用户信息
     */
    @PostMapping("/user")
    public Object saveUser(@RequestBody User user) {
        if (user.getUsername() == null) {
            throw new OAuth2Exception("用户名不能为空", Constants.ERROR_STATUS_INVALID_PARAMETER, Constants.ERROR_INVALID_REQUEST);
        }
        if (user.getPassword() == null) {
            throw new OAuth2Exception("密码不能为空", Constants.ERROR_STATUS_INVALID_PARAMETER, Constants.ERROR_INVALID_REQUEST);
        }
        user.setStatus(0);
        if (user.getNickname() == null) {
            user.setNickname("用户昵称");
        }
        oauth2Service.saveUser(user);
        return "OK";
    }
}
