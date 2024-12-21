package com.example.domain.model;

import com.alibaba.excel.write.handler.CellWriteHandler;
import lombok.*;

import java.util.Collection;
import java.util.List;

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
     * 自定义处理器：单元格合并、背景色等
     */
    private List<CellWriteHandler> cellWriteHandlers;

    /**
     * 类型
     */
    @NonNull
    private Class<?> clazz;

    /**
     * 数据行 List<List<Object>>
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