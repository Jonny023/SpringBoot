package org.example.controller;

import ch.qos.logback.core.util.CloseUtil;
import com.google.common.collect.Maps;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.vo.ProductVO;
import org.example.utils.PdfUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.ui.freemarker.SpringTemplateLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@RestController
public class DemoController {

    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("pdf")
    public void test2(HttpServletResponse response, HttpServletRequest request) throws Exception {

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/pdf");
        OutputStream out = response.getOutputStream();
        Writer writer = null;
        ByteArrayOutputStream outStream = null;

        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
            cfg.setClassicCompatible(true);

            // 模板路径
            // String templatePath = "src/main/resources";

            // 获取模版路径
            // Resource resource = new ClassPathResource("/templates/temp.ftl");
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            SpringTemplateLoader templateLoader = new SpringTemplateLoader(resourceLoader, "classpath:/templates/");
            cfg.setTemplateLoader(templateLoader);
            cfg.setDefaultEncoding(StandardCharsets.UTF_8.name());

            // cfg.setDirectoryForTemplateLoading(resource.getFile());
            Template freemarkerTemplate = cfg.getTemplate("temp.ftl");
            outStream = new ByteArrayOutputStream();
            writer = new OutputStreamWriter(outStream, StandardCharsets.UTF_8);

            Map<String, Object> map = Maps.newHashMap();
            map.put("products", ProductVO.batch());

            // 数据传递
            freemarkerTemplate.process(map, writer);
            String htmlContent = outStream.toString(StandardCharsets.UTF_8.name());

            log.info("{}", htmlContent);
            PdfUtil.buildPdf(out, htmlContent);
        } catch (Exception e) {
            log.error("导出失败！", e);
        } finally {
            CloseUtil.closeQuietly(outStream);
            CloseUtil.closeQuietly(out);
            CloseUtil.closeQuietly(writer);
        }
    }
}
