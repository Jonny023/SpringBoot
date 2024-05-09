package com.example;

import org.example.SpringBootWsDemoApplication;
import org.example.domain.vo.ProductVO;
import org.junit.jupiter.api.Test;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@SpringBootTest(classes = SpringBootWsDemoApplication.class)
@TestPropertySource(properties = {"app.template.path=classpath:/templates/jxl_temp_img.xlsx"})
public class ExportImgTest {

    @Value("${app.template.path}")
    private Resource templateResource;

    @Test
    public void test() throws IOException {
        List<ProductVO> batch = ProductVO.batchToJxls();

        // 创建上下文并设置数据
        Context context = new Context();
        context.putVar("products", batch);

        // 加载模板
        OutputStream os = new FileOutputStream("output_img.xlsx");
        // 获取模板文件输入流
        // InputStream templateStream = ExportTest.class.getResourceAsStream("templates/jxl_temp.xlsx");
        // JxlsHelper.getInstance().processTemplate(templateStream, os, context);
        // imageType图片类型默认是PNG，支持：PNG, JPEG, EMF, WMF, PICT, DIB，模板中src是model中传入的字节数组byte[]
        JxlsHelper instance = JxlsHelper.getInstance();
        instance.processTemplate(templateResource.getInputStream(), os, context);
    }

}