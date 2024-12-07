package com.example.domain.model;

import lombok.*;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportData {

    /**
     * 动态表头
     */
    @NonNull
    private List<List<String>> head;

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