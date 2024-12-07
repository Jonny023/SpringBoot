package com.example.domain.model;

import com.alibaba.excel.write.handler.CellWriteHandler;
import lombok.*;

import java.util.Collection;
import java.util.List;

/**
 * @author Jonny
 * @description 表头固定行+动态表头
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportFixedAndDynamicData {

    /**
     * 合并
     */
    private List<CellWriteHandler> cellWriteHandlers;

    /**
     * 动态表头
     */
    @NonNull
    private List<List<String>> dynamicHead;

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