package org.sj.oauth2.configuration;

import org.sj.oauth2.exception.OAuth2Exception;
import org.sj.oauth2.util.Constants;
import org.sj.oauth2.util.OAuth2Response;
import org.sj.oauth2.web.interceptor.LoginHandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * web配置
 *
 * @author shijian
 * @email shijianws@163.com
 * @date 2017-12-30
 */
@Configuration
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
@RestControllerAdvice
public class WebConfiguration {
    @Configuration
    public class InterceptorConfigurer extends WebMvcConfigurerAdapter {
        @Autowired
        private LoginHandlerInterceptor loginHandlerInterceptor;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            // 注册拦截器
            registry.addInterceptor(loginHandlerInterceptor);
        }
    }

    @Autowired(required = false)
    private List<ErrorViewResolver> errorViewResolvers;

    private ServerProperties serverProperties;

    public WebConfiguration(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    /**
     * http status 异常处理器, 拦截response.sendError();
     */
    @Bean
    public BasicErrorController basicErrorController(ErrorAttributes attributes) {
        final Logger LOGGER = LoggerFactory.getLogger(WebConfiguration.class);
        return new BasicErrorController(attributes, serverProperties.getError(), errorViewResolvers) {
            @Override
            public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
                Map<String, Object> errorAttributes = new HashMap<>(super.getErrorAttributes(request, false));
                LOGGER.error("出现HTTP错误: {}", errorAttributes);
                String message = (String) errorAttributes.get("message");
                int status = (Integer) errorAttributes.get("status");
                try {
                    // 将 http 异常转换 应用异常
                    WebConfiguration.this.exceptionHandler(request, response, new OAuth2Exception(message, status, Constants.ERROR_INVALID_REQUEST));
                } catch (Exception e) {
                    LOGGER.error("{}", e.getMessage());
                }
                return null;
            }
        };
    }

    /**
     * 应用异常处理器
     */
    @ExceptionHandler
    public void exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) throws Exception {
        // 错误信息
        String message = ex.getMessage();
        // 错误状态码
        int status;
        // 错误类型
        String error = Constants.ERROR_INVALID_REQUEST;
        Integer errcode = null;
        if (ex instanceof OAuth2Exception) {
            OAuth2Exception e = (OAuth2Exception) ex;
            status = e.getStatus();
            error = e.getError();
            errcode = e.getErrcode();
        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            // 请求方式不被支持
            status = Constants.ERROR_STATUS_UNSUPPORTED_METHOD;
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            // 请求体类型不被支持
            status = Constants.ERROR_STATUS_UNSUPPORTED_MEDIA_TYPE;
        } else if (ex instanceof HttpMessageNotReadableException) {
            // 请求体类型不被支持
            status = Constants.ERROR_STATUS_INVALID_PARAMETER;
            message = "缺少请求体数据";
        } else {
            // 默认请求参数非法
            status = Constants.ERROR_STATUS_INVALID_PARAMETER;
        }
        if (status == Constants.ERROR_STATUS_INVALID_AUTHORIZATION) {
            // 401需要登录
            response.setStatus(status);
            response.addHeader(Constants.HTTP_HEADER_WWW_AUTHENTICATE, String.format("Basic realm=\"OAuth2\", error=\"%s\", error_description=\"%s\"", error, message));
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.ERROR, error);
        map.put(Constants.ERROR_DESCRIPTION, message);
        map.put(Constants.ERROR_STATUS, status);
        if (errcode != null) {
            map.put(Constants.ERRCODE, errcode);
        }
        OAuth2Response resp = new OAuth2Response(response, map);
        String redirectUri = request.getParameter(Constants.REDIRECT_URI);
        if (redirectUri == null) {
            resp.redirectUri(redirectUri);
        }
        resp.response();
    }
}
