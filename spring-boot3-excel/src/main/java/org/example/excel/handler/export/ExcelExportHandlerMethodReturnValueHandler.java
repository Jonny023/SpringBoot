package org.example.excel.handler.export;

import cn.hutool.http.Header;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.excel.annotation.ExcelExport;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static cn.hutool.poi.excel.ExcelUtil.XLS_CONTENT_TYPE;

/**
 * @author Jonny
 * @description: 通用excel导出响应数据处理
 */
@Component
@Slf4j
public class ExcelExportHandlerMethodReturnValueHandler extends CustomHandlerMethodReturnValueHandler {

    private final ApplicationContext applicationContext;

    public ExcelExportHandlerMethodReturnValueHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.hasMethodAnnotation(ExcelExport.class);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        ExcelExport excelExport = returnType.getMethodAnnotation(ExcelExport.class);

        if (response == null || excelExport == null) {
            handleError(response, "Invalid response or @ExcelExport annotation");
            return;
        }

        mavContainer.setRequestHandled(true);

        if (!(returnValue instanceof List<?> returnList)) {
            handleError(response, "Return value is null or not supported type, cannot build excel");
            return;
        }

        String fileName = getFileName(excelExport);
        setResponseHeaders(response, fileName, excelExport.fileSuffix());

        try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build()) {
            WriteSheet sheet = EasyExcel.writerSheet(0).head(excelExport.dataClass()).build();
            excelWriter.write(returnList, sheet);
        }
    }

    private String getFileName(ExcelExport excelExport) {
        String fileName = excelExport.fileName();
        Class<? extends FileNameHandler> fileNameHandlerClass = excelExport.fileNameHandler();
        return Optional.ofNullable(fileNameHandlerClass).map(applicationContext::getBean).map(handler -> handler.getFileName(fileName)).orElse(fileName);
    }

    private void setResponseHeaders(HttpServletResponse response, String fileName, String fileSuffix) {
        response.setContentType(XLS_CONTENT_TYPE);
        response.setHeader(Header.CONTENT_DISPOSITION.getValue(), String.format("attachment;filename=%s%s", URLEncoder.encode(fileName, StandardCharsets.UTF_8), fileSuffix));
    }

    private void handleError(HttpServletResponse response, String errorMessage) throws Exception {
        log.warn(errorMessage);
        if (response != null) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(errorMessage);
            response.getWriter().flush();
        }
    }
}