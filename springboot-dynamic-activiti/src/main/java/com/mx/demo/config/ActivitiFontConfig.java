package com.mx.demo.config;

import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.context.annotation.Configuration;

/**
 *  写出流程中文乱码过滤
 */
@Configuration
public class ActivitiFontConfig implements ProcessEngineConfigurationConfigurer {

    /**
     * 解決工作流生成图片乱码问题 注：可以解决任务的乱码问题，但是箭头上的name名称不显示，
     * 有兴趣的可以进行百度，我百度的是要改源码，懒得弄了。  还有箭头条件的添加，也会出现相应的问题，有的博客说是加入条件必须使用排他网关
     * @param processEngineConfiguration processEngineConfiguration
     */
    @Override
    public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
        processEngineConfiguration.setActivityFontName("宋体");
        processEngineConfiguration.setAnnotationFontName("宋体");
        processEngineConfiguration.setLabelFontName("宋体");
    }
}