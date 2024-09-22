package ru.mastkey.ripperstarter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.mastkey.ripperstarter.aop.ExecutionTimeAspect;
import ru.mastkey.ripperstarter.context.PostProxyPostConstructInvokerContextListener;

@Configuration(proxyBeanMethods = false)
@EnableAspectJAutoProxy
public class StarterAutoConfiguration {

    @Bean
    public ExecutionTimeAspect executionTimeAspect() {
        return new ExecutionTimeAspect();
    }

    @Bean
    public PostProxyPostConstructInvokerContextListener postProxyPostConstructInvokerContextListener() {
        return new PostProxyPostConstructInvokerContextListener();
    }
}
