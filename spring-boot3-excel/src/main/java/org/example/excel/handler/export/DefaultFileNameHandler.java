package org.example.excel.handler.export;

/**
 * 默认文件名处理器
 *
 * @author Jonny
 */
public class DefaultFileNameHandler implements FileNameHandler {

    /**
     * 获取文件名
     *
     * @param fileName 文件名
     * @return 文件名
     */
    @Override
    public String getFileName(String fileName) {
        return fileName;
    }
}