package org.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Resource
    private AlipayInterceptor alipayInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(alipayInterceptor).addPathPatterns("/**").excludePathPatterns("/alipay/**");
    }
}
