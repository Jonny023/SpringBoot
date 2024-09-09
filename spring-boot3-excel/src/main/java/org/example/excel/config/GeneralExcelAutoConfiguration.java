package org.example.excel.config;

import org.example.excel.handler.export.CustomHandlerMethodReturnValueHandler;
import org.example.excel.handler.export.DefaultFileNameHandler;
import org.example.excel.handler.imports.CustomHandlerMethodArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * excel文件上传、下载自动装配类
 *
 * @author Jonny
 */
@Component
// @AutoConfiguration
public class GeneralExcelAutoConfiguration {

    /**
     * 添加自定义的excel文件上传下载配置类到spring容器中
     */
    @Bean
    public WebMvcConfigurer excelUploadDownloadWebMvcConfigurer(List<CustomHandlerMethodReturnValueHandler> customHandlerMethodReturnValueHandlers
            , List<CustomHandlerMethodArgumentResolver> customHandlerMethodArgumentResolvers) {
        return new GeneralExcelWebMvcConfig(customHandlerMethodReturnValueHandlers, customHandlerMethodArgumentResolvers);
    }

    /**
     * 添加自定义的文件下载处理器是否为最优先处理器配置类到spring容器中
     *
     * @return GeneralExcelHandlerConfig
     */
    @Bean
    public GeneralExcelHandlerConfig downloadHandlerFirstConfig(List<CustomHandlerMethodReturnValueHandler> customHandlerMethodReturnValueHandlers) {
        return new GeneralExcelHandlerConfig(customHandlerMethodReturnValueHandlers);
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    /**
     * 添加默认的文件名处理器到spring容器中
     *
     * @return 默认文件名生成规则
     */
    @Bean
    public DefaultFileNameHandler defaultFileNameHandler() {
        return new DefaultFileNameHandler();
    }
}
