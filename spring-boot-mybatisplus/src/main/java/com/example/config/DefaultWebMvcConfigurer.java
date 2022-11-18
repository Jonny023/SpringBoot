package com.example.config;

import com.example.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class DefaultWebMvcConfigurer implements WebMvcConfigurer {

    @Resource
    private LoginInterceptor loginInterceptor;

    @Value("${web.resource-locations}")
    private String webResourceLocations;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] excludeUrl = new String[] {
            "/user/login", "/doc.html", "/swagger-resources", "/webjars/**", "/favicon.ico",
                "/task/download/**", "/*", "/web/**"
        };
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns(excludeUrl);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //过滤swagger
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler("/swagger-resources/**")
                .addResourceLocations("classpath:/META-INF/resources/swagger-resources/");

        registry.addResourceHandler("/swagger/**")
                .addResourceLocations("classpath:/META-INF/resources/swagger*");

        registry.addResourceHandler("/v2/api-docs/**")
                .addResourceLocations("classpath:/META-INF/resources/v2/api-docs/");

        registry.addResourceHandler("/web/**")
                .addResourceLocations(webResourceLocations);
    }

}