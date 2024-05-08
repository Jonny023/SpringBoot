package org.example.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.openhtmltopdf.bidi.BidiSplitterFactory;
import com.openhtmltopdf.bidi.SimpleBidiSplitterFactory;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.core.env.Environment;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.OutputStream;

public class PdfUtil {

    private static File file = null;

    static {
        Environment env = SpringUtil.getBean(Environment.class);
        String fontPath = env.getProperty("fontPath");
        file = new File(fontPath);
    }

    /**
     * @param os
     * @param htmlContent
     * @throws Exception
     */
    public static void buildPdf(OutputStream os, String htmlContent) throws Exception {
        PdfRendererBuilder builder = new PdfRendererBuilder();

        // 这种方式加载字体文件在本地可以运行，打包jar后获取不到字体，所以将字体存放到本地磁盘，而不是从springboot.jar!/BOOT-INF/classes!/templates/fonts/simsun.ttf读取
        // builder.useFont(ResourceUtils.getFile("classpath:templates/fonts/simsun.ttf"), "simsun");

        // 设置 PDF 渲染器使用的字符编码为 UTF-8
        builder.useFont(file, "simsun");

        builder.useFastMode();
        builder.withHtmlContent(htmlContent, ResourceUtils.getURL("classpath*:templates/img/").toString());
        builder.toStream(os);
        builder.run();
    }
}
