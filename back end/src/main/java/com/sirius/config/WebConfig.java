package com.sirius.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

// 用于处理跨域问题(可参考这种格式):
@Configuration
public class WebConfig{
    @Bean
    // 解决跨域访问问题
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");    // 设置访问源地址(全部)
        corsConfiguration.addAllowedHeader("*");    // 设置访问请求头
        corsConfiguration.addAllowedMethod("*");    // 设置访问请求方法
        source.registerCorsConfiguration("/**",corsConfiguration);  // 对接口配置跨域设置
        return new CorsFilter(source);
    }
}