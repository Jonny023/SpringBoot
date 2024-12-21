package com.example.domain.model;

import com.alibaba.excel.write.handler.CellWriteHandler;
import lombok.*;

import java.util.Collection;
import java.util.List;

/**
 * @author Jonny
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportData {

    /**
     * 自定义处理器：单元格合并、背景色等
     */
    private List<CellWriteHandler> cellWriteHandlers;

    /**
     * 动态表头
     */
    @NonNull
    private List<List<String>> head;

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