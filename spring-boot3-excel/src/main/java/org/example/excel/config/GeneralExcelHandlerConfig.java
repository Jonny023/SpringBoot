package org.example.excel.config;

import org.example.excel.handler.export.CustomHandlerMethodReturnValueHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 通用excel导出处理器配置（配置优先执行）
 *
 * @author Jonny
 */
public class GeneralExcelHandlerConfig implements ApplicationContextAware {

    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;
    private final List<CustomHandlerMethodReturnValueHandler> customHandlerMethodReturnValueHandlers;

    public GeneralExcelHandlerConfig(List<CustomHandlerMethodReturnValueHandler> customHandlerMethodReturnValueHandlers) {
        this.customHandlerMethodReturnValueHandlers = customHandlerMethodReturnValueHandlers;
    }

    /**
     * 设置上下文
     *
     * @param applicationContext 上下文
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.requestMappingHandlerAdapter = applicationContext.getBean(RequestMappingHandlerAdapter.class);
        setHandlerPriority();
    }

    /**
     * 设置处理器优先级
     */
    public void setHandlerPriority() {
        // 获取当前处理器的所有处理器
        List<HandlerMethodReturnValueHandler> returnValueHandlers = this.requestMappingHandlerAdapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> newReturnValueHandlers = new ArrayList<>(this.customHandlerMethodReturnValueHandlers);
        // 添加原有的处理器
        Optional.ofNullable(returnValueHandlers).ifPresent(newReturnValueHandlers::addAll);
        // 设置新的处理器
        requestMappingHandlerAdapter.setReturnValueHandlers(newReturnValueHandlers);
    }
}