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

import java.io.*;
import java.util.List;

@SpringBootTest(classes = SpringBootWsDemoApplication.class)
@TestPropertySource(properties = {"app.template.path=classpath:/templates/jxl_temp.xlsx"})
public class ExportTest {

    @Value("${app.template.path}")
    private Resource templateResource;

    @Test
    public void test() throws IOException {
        List<ProductVO> batch = ProductVO.batch();

        // 创建上下文并设置数据
        Context context = new Context();
        context.putVar("products", batch);

        // 加载模板
        OutputStream os = new FileOutputStream("output.xlsx");
        // 获取模板文件输入流
        // InputStream templateStream = ExportTest.class.getResourceAsStream("templates/jxl_temp.xlsx");
        // JxlsHelper.getInstance().processTemplate(templateStream, os, context);
        JxlsHelper.getInstance().processTemplate(templateResource.getInputStream(), os, context);
    }

}