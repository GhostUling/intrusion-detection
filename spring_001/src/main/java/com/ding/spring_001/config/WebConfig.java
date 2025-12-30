package com.ding.spring_001.config;

import com.ding.spring_001.common.JwtInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration//声明这是一个配置类，Spring在启动时会加载并扫描这个类中的配置信息。
public class WebConfig implements WebMvcConfigurer {//WebMvcConfigurer 是 Spring MVC 提供的一个接口，用于配置 Spring MVC 的行为。

    @Resource
    JwtInterceptor jwtInterceptor;

    //设置自定义JWT拦截器 并配置拦截规则
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/api/**").excludePathPatterns("/api/user/login",
                "/api/user/register"
        );
    }

    //这是 WebMvcConfigurer 接口中的一个方法，用于配置路径匹配规则。
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        /**
         * addPathPrefix 方法用于添加一个路径前缀和一个断言（Predicate）。
         * /api 是要添加的路径前缀，这意味着所有以 /api 开头的请求路径都会被这个配置所影响。
         * clazz -> clazz.isAnnotationPresent(RestController.class) 是一个断言，用于判断处理请求的控制器类（Controller）是否标注了 @RestController 注解。
         * 如果一个 Controller 类标注了 @RestController 注解，则这个路径前缀会生效，即相关的请求将会被映射到这些标注了 @RestController 的控制器类上。
         */
        configurer.addPathPrefix("/api", clazz -> clazz.isAnnotationPresent(RestController.class));
    }
}