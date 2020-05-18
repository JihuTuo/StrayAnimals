package com.shiep.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 使用cors解决跨域问题(Spring MVC 提供了这样一个过滤器)
 */
@Configuration
public class SACorsConfiguration {
    @Bean
    public CorsFilter corsFilter() {

        // 初始化cors配置对象
        CorsConfiguration corsConfiguration = new CorsConfiguration();
            // 设置允许跨域的域名（也可以使用setAllowedOrigins()方法，但是参数是List集合，比较麻烦）
        corsConfiguration.addAllowedOrigin("http://www.sa.com");
        corsConfiguration.addAllowedOrigin("http://manage.sa.com");
        corsConfiguration.addAllowedOrigin("http://127.0.0.1:9002");
        corsConfiguration.addAllowedOrigin("http://127.0.0.1:9001");
            // 允许携带Cookie，此时上面允许跨域访问的域名就不能写成“*”，即表示所有域名都可以跨域
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedMethod("*");   // 代表所有的方法：POST PUT GET DELETE。。。
        corsConfiguration.addAllowedHeader("*");   // 允许携带任何头信息

        // 初始化cors配置源对象
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        // 返回corsFileter实例，参数：cors配置源对象
        return new CorsFilter(corsConfigurationSource);
    }
}
