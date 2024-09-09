package org.example.excel.handler.export;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static cn.hutool.core.date.DatePattern.PURE_DATETIME_FORMATTER;

/**
 * @author Jonny
 * @description: 导出文件名+时间戳（防止文件名重复）
 */
@Component
public class ExportFileNameHandler implements FileNameHandler {

    @Override
    public String getFileName(String fileName) {
        return String.format("%s_%s", fileName, LocalDateTime.now().format(PURE_DATETIME_FORMATTER));
    }
}