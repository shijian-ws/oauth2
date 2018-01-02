package org.sj.oauth2.configuration;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Properties;

/**
 * 配置声明式事务, 如果不加@EnableTransactionManagement则会被org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration#transactionInterceptor()创建的实例覆盖
 */
@Configuration
@EnableTransactionManagement
public class TransactionConfiguration {
    /**
     * 定义事务切入点,方法名称就是Bean名称
     */
    @Bean
    public TransactionInterceptor transactionInterceptor(PlatformTransactionManager manager) {
        Properties props = new Properties();
        props.setProperty("authorization", "PROPAGATION_REQUIRED,-java.lang.RuntimeException");
        props.setProperty("accessToken", "PROPAGATION_REQUIRED,-java.lang.RuntimeException");
        props.setProperty("refreshToken", "PROPAGATION_REQUIRED,-java.lang.RuntimeException");
        props.setProperty("save*", "PROPAGATION_REQUIRED,-java.lang.RuntimeException");
        props.setProperty("*", "PROPAGATION_NOT_SUPPORTED,readOnly");
        return new TransactionInterceptor(manager, props);
    }

    /**
     * 织入
     */
    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor(TransactionInterceptor interceptor) {
        // 切入点
        AspectJExpressionPointcut expression = new AspectJExpressionPointcut();
        expression.setExpression("execution(* org.sj.oauth2.service.tx.I*Service.*(..))");
        return new DefaultPointcutAdvisor(expression, interceptor);
    }
}