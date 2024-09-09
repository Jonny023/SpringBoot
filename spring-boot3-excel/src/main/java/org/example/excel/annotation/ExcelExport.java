package org.example.excel.annotation;

import org.example.excel.handler.export.DefaultFileNameHandler;
import org.example.excel.handler.export.FileNameHandler;

import java.lang.annotation.*;

/**
 * @author Jonny
 * @description: 下载Excel注解
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelExport {

    /**
     * 文件名
     *
     * @return 文件名
     */
    String fileName();

    /**
     * 文件名处理器
     *
     * @return 文件名处理器
     */
    Class<? extends FileNameHandler> fileNameHandler() default DefaultFileNameHandler.class;

    /**
     * 文件格式后缀
     */
    String fileSuffix() default ".xlsx";

    /**
     * 数据类
     */
    Class<?> dataClass();

}