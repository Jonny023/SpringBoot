package com.example.springbootlogin.config;

import com.example.springbootlogin.interceptor.ApiInterceptor;
import com.example.springbootlogin.interceptor.BackgroundInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginInterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiInterceptor()).addPathPatterns("/api/**").excludePathPatterns("/api/login");
        registry.addInterceptor(new BackgroundInterceptor()).addPathPatterns("/bg/**").excludePathPatterns("/bg/login");
    }
}