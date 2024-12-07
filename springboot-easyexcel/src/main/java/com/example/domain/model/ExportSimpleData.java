package com.example.domain.model;

import lombok.*;

import java.util.Collection;

/**
 * @author Jonny
 * @description 简单导出
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportSimpleData {

    /**
     * 类型
     */
    @NonNull
    private Class<?> clazz;

    /**
     * 数据行
     */
    @NonNull
    private Collection<?> data;

    /**
     * excel名称
     */
    @NonNull
    private String excelName;

    /**
     * 工作表名称
     */
    @Builder.Default
    private String sheetName = "sheet1";

}