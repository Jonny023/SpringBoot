package org.example.excel.config;

import org.example.excel.handler.export.CustomHandlerMethodReturnValueHandler;
import org.example.excel.handler.imports.CustomHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Excel通用导入导出配置类
 *
 * @author Jonny
 */
public class GeneralExcelWebMvcConfig implements WebMvcConfigurer {

    private final List<CustomHandlerMethodArgumentResolver> customArgumentResolvers;
    private final List<CustomHandlerMethodReturnValueHandler> customReturnValueHandlers;

    public GeneralExcelWebMvcConfig(List<CustomHandlerMethodReturnValueHandler> customReturnValueHandlers,
                                    List<CustomHandlerMethodArgumentResolver> customArgumentResolvers) {
        this.customArgumentResolvers = customArgumentResolvers;
        this.customReturnValueHandlers = customReturnValueHandlers;
    }

    /**
     * 添加自定义的参数处理器
     *
     * @param resolvers 参数处理器列表
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.addAll(this.customArgumentResolvers);
    }

    /**
     * 添加自定义的返回值处理器
     *
     * @param handlers 返回值处理器列表
     */
    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        handlers.addAll(this.customReturnValueHandlers);
    }

}
