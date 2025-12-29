package com.ding.spring_001.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        //用于配置 CORS 规则的基于 URL 的配置源。
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");//允许所有来源地址访问
        corsConfiguration.addAllowedHeader("*");//允许所有的请求头访问
        corsConfiguration.addAllowedMethod("*");//允许所有的请求方法（get、post等）访问
        //对接口配置跨域设置，将上述配置应用到所有路径
        source.registerCorsConfiguration("/**",corsConfiguration);
        return new CorsFilter(source);
    }
}