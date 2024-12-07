package com.example.controller;

import com.example.domain.model.ExportData;
import com.example.domain.model.ExportFixedAndDynamicData;
import com.example.domain.model.ExportSimpleData;
import com.example.domain.model.merge.MergeCellWriteHandler;
import com.example.domain.vo.DataDemo;
import com.example.service.DynamicExcelService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Jonny
 */
@Controller
@RequestMapping("/file")
public class DownloadController {

    @Resource
    private DynamicExcelService excelService;

    /**
     * 复杂动态表头导出
     */
    @GetMapping("/dynamic")
    public void dynamic() {
        ExportData data = ExportData.builder()
                .excelName("动态表头数据")
                .head(builderHeader())
                .data(getDataList()).build();
        excelService.export(data);
    }

    /**
     * 固定表头+复杂动态表头导出
     */
    @GetMapping("/fix_dynamic")
    public void download() {
        List<List<String>> dynamicHead = builderHeader1();
        ExportFixedAndDynamicData data = ExportFixedAndDynamicData.builder()
                .excelName("固定表头+动态表头数据")
                .cellWriteHandlers(Lists.newArrayList(new MergeCellWriteHandler()))
                .dynamicHead(dynamicHead)
                .data(getDataList()).build();
        excelService.exportFixedAndDynamicHead(data);
    }

    /**
     * 简单固定表头导出
     */
    @GetMapping("/simple")
    public void exportSimple() {
        Class<DataDemo> dataDemoClass = DataDemo.class;
        ExportSimpleData data = ExportSimpleData.builder()
                .excelName("简单固定表头")
                .clazz(dataDemoClass)
                .sheetName("学生成绩")
                .data(builderData()).build();
        excelService.exportSimple(data);
    }

    private List<DataDemo> builderData() {
        List<DataDemo> dataDemos = Lists.newArrayList(
                DataDemo.builder().no("S001").stuName("小明").ywScore(new BigDecimal("85.5")).sxScore(new BigDecimal("88.9")).build(),
                DataDemo.builder().no("S010").stuName("小丽").ywScore(new BigDecimal("99")).sxScore(new BigDecimal("77.2")).build(),
                DataDemo.builder().no("S011").stuName("小王").ywScore(new BigDecimal("76")).sxScore(new BigDecimal("83")).build()
        );
        dataDemos.forEach(DataDemo::calc);
        return dataDemos;
    }

    /**
     * 构建表头
     *
     * @return 表头数据
     */
    private static List<List<String>> builderHeader() {

        // 主表头
        List<List<String>> topTitle = Lists.newArrayList();
        // 第1列
        topTitle.add(Lists.newArrayList("编码"));
        // 第2列
        topTitle.add(Lists.newArrayList("名称"));
        // 动态列
        List<String> secondTitles = Lists.newArrayList("2024", "2025", "2026", "2027");
        // 动态子列
        List<String> threeTitles = Lists.newArrayList("数量", "金额");

        // 根据实际需要，决定要渲染多少列
        secondTitles.forEach(second -> threeTitles.forEach(three -> topTitle.add(Lists.newArrayList(second, three))));

        // 后面的列
        topTitle.add(Lists.newArrayList("地址"));
        return topTitle;
    }

    private static List<List<String>> builderHeader1() {

        // 主表头
        List<List<String>> topTitle = Lists.newArrayList();
        topTitle.add(Lists.newArrayList("总览", "编码"));
        topTitle.add(Lists.newArrayList("总览", "名称"));
        // 动态列
        List<String> secondTitles = Lists.newArrayList("2024", "2025", "2026", "2027");
        // 动态子列
        List<String> threeTitles = Lists.newArrayList("数量", "金额");

        // 根据实际需要，决定要渲染多少列
        secondTitles.forEach(second -> threeTitles.forEach(three -> topTitle.add(Lists.newArrayList("总览", second, three))));

        // 后面的列
        topTitle.add(Lists.newArrayList("总览", "地址"));
        return topTitle;
    }

    /**
     * 构建数据
     *
     * @return 行数据
     */
    private static List<List<Object>> getDataList() {
        List<List<Object>> contentList = Lists.newArrayList();
        contentList.add(Lists.newArrayList("A001", "零件A", 10, 20.1, 20, 200.01, 20, 200.01, 20, 200.01, "重庆"));
        contentList.add(Lists.newArrayList("BC01", "零件B", 20, 300, 40, 500.8, 40, 500.8, 40, 500.8, "四川"));
        return contentList;
    }


}
