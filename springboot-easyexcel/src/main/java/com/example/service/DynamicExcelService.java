package com.example.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.example.domain.model.ExportData;
import com.example.domain.model.ExportFixedAndDynamicData;
import com.example.domain.model.ExportSimpleData;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.ehcache.core.util.CollectionUtil;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static org.apache.poi.hpsf.ClassIDPredefined.EXCEL_V11;

/**
 * @author Jonny
 * @description 动态excel导出
 */
@Service
public class DynamicExcelService {

    /**
     * 导出复杂表头数据
     *
     * @param data 数据信息
     */
    public void export(ExportData data) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Optional.ofNullable(attributes).ifPresent(attr -> {
            HttpServletResponse response = attr.getResponse();
            Optional.ofNullable(response).ifPresent(resp -> {
                resp.setContentType(EXCEL_V11.getContentType());
                resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

                ExcelWriter writer = null;
                try {
                    // 防止中文乱码
                    String fileName = URLEncoder.encode(data.getExcelName() + ".xlsx", StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
                    resp.setHeader(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(fileName).build().toString());
                    resp.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
                    writer = EasyExcelFactory.write(resp.getOutputStream()).registerWriteHandler(cellStyleStrategy()).build();
                    // writer = EasyExcelFactory.write(response.getOutputStream()).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();

                    // 动态添加表头
                    WriteSheet sheet1 = new WriteSheet();
                    sheet1.setSheetName(data.getSheetName());
                    sheet1.setSheetNo(0);

                    // 创建工作表
                    WriteTable table = new WriteTable();
                    table.setTableNo(1);
                    // 设置表头
                    table.setHead(data.getHead());
                    // 写数据
                    writer.write(data.getData(), sheet1, table);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (Objects.nonNull(writer)) {
                        writer.finish();
                    }
                }
            });
        });
    }

    /**
     * 导出固定表头
     *
     * @param data 数据信息
     */
    public void exportSimple(ExportSimpleData data) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Optional.ofNullable(attributes).ifPresent(attr -> {
            HttpServletResponse response = attr.getResponse();
            Optional.ofNullable(response).ifPresent(resp -> {
                resp.setContentType(EXCEL_V11.getContentType());
                resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

                try {
                    // 防止中文乱码
                    String fileName = URLEncoder.encode(data.getExcelName() + ".xlsx", StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
                    resp.setHeader(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(fileName).build().toString());
                    resp.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
                    EasyExcel.write(resp.getOutputStream(), data.getClazz()).registerWriteHandler(cellStyleStrategy())
                            .sheet(data.getSheetName())
                            .doWrite(data.getData());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

    /**
     * 导出固定行和动态表头数据
     *
     * @param data 参数
     */
    public void exportFixedAndDynamicHead(ExportFixedAndDynamicData data) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Optional.ofNullable(attributes).ifPresent(attr -> {
            HttpServletResponse response = attr.getResponse();
            Optional.ofNullable(response).ifPresent(resp -> {
                resp.setContentType(EXCEL_V11.getContentType());
                resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

                ExcelWriter writer = null;
                try {
                    // 防止中文乱码
                    String fileName = URLEncoder.encode(data.getExcelName() + ".xlsx", StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
                    resp.setHeader(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(fileName).build().toString());
                    resp.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);

                    // 初始化 writer
                    ExcelWriterBuilder write = EasyExcelFactory.write(resp.getOutputStream());
                    if (!CollectionUtils.isEmpty(data.getCellWriteHandlers())) {
                        data.getCellWriteHandlers().forEach(write::registerWriteHandler);
                    }
                    writer = write.registerWriteHandler(cellStyleStrategy()).build();

                    // 创建 WriteSheet
                    WriteSheet sheet1 = new WriteSheet();
                    sheet1.setSheetName(data.getSheetName());
                    sheet1.setSheetNo(0);

                    // 创建 WriteTable
                    WriteTable table = new WriteTable();
                    table.setTableNo(1);

                    // 设置动态表头
                    table.setHead(data.getDynamicHead());

                    //写入动态表头和数据
                    writer.write(data.getData(), sheet1, table);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (Objects.nonNull(writer)) {
                        writer.finish();
                    }
                }
            });
        });
    }


    /**
     * 配置字体，表头背景等
     *
     * @return 样式
     */
    public HorizontalCellStyleStrategy cellStyleStrategy() {

        // 表头
        WriteCellStyle headCellStyle = new WriteCellStyle();
        // 背景色
        headCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

        WriteFont headWriteFont = new WriteFont();
        // 加粗
        headWriteFont.setBold(true);
        headCellStyle.setWriteFont(headWriteFont);

        // 单元格样式
        WriteCellStyle cellStyle = new WriteCellStyle();
        // 字体策略
        WriteFont font = new WriteFont();
        cellStyle.setWriteFont(font);
        // 设置边框+数据垂直居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 数据行水平居中
        cellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderLeft(BorderStyle.NONE);
        cellStyle.setBorderTop(BorderStyle.NONE);
        cellStyle.setBorderRight(BorderStyle.NONE);
        cellStyle.setBorderBottom(BorderStyle.NONE);

        // 自动换行
        cellStyle.setWrapped(true);
        // 表头样式
        return new HorizontalCellStyleStrategy(headCellStyle, cellStyle);
    }
}