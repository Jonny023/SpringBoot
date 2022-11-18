package com.example.util;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 *  文件下载中文乱码
 */
public class FileDownloadUtil {

    /**
     * 根据不同浏览器 User-Agent，生成不同的 Content_disposition
     *
     * @param fileName 文件名
     * @param format 格式：zip、png...
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getContentDisposition(String fileName, String format, HttpServletRequest request) throws UnsupportedEncodingException {
        String content_disposition = "";
        String userAgent = request.getHeader("User-Agent");
        // 针对IE或者以IE为内核的浏览器：
        if (userAgent.contains("Safari")) {
            // name.getBytes("UTF-8")处理safari的乱码问题
            byte[] bytes = fileName.getBytes("UTF-8");
            // 各浏览器基本都支持ISO编码
            fileName = new String(bytes, "ISO-8859-1");
            // 文件名外的双引号处理firefox的空格截断问题
            content_disposition = String.format("attachment; filename=\"%s\".%s", fileName, format);
        } else {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            content_disposition = "attachment;filename=" + fileName;
        }
        return content_disposition;
    }
}
