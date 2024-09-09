package org.example.excel.handler.export;

/**
 * 文件名处理器
 */
public interface FileNameHandler {

    /**
     * 获取文件名
     *
     * @return 文件名
     */
    String getFileName(String fileName);
}