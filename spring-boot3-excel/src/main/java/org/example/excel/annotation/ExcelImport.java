package org.example.excel.annotation;

import java.lang.annotation.*;

/**
 * @author Jonny
 * @description: 上传Excel注解
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelImport {

    /**
     * 文件名
     */
    String name();

    /**
     * 数据最大行数
     */
    int maxRow() default 0;

    /**
     * 数据最小行数
     */
    int minRow() default 0;

    /**
     * 数据类型
     */
    Class<?> dataClass();

    /**
     * 验证头信息
     *
     * @return 是否验证头信息
     */
    boolean validateHead() default true;

}