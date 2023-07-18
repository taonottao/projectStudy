package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/17 18:57
 */
@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/img/**")
                .excludePathPatterns("/js/**")
                .excludePathPatterns("/admin_login.html")
                .excludePathPatterns("/login.html")
                .excludePathPatterns("/problem_list.html")
                .excludePathPatterns("/reg.html")
                .excludePathPatterns("/problem/selectall")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/reg");
    }
}
