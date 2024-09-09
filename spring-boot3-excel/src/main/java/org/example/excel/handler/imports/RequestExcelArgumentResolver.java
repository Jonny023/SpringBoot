package org.example.excel.handler.imports;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.excel.annotation.ExcelImport;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel文档上传处理器
 *
 * @author Jonny
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RequestExcelArgumentResolver extends CustomHandlerMethodArgumentResolver {

    private final Validator validator;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ExcelImport.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        ExcelImport excelImport = parameter.getParameterAnnotation(ExcelImport.class);
        Assert.notNull(excelImport, "No @ExcelImport annotation found");

        MultipartRequest request = webRequest.getNativeRequest(MultipartRequest.class);
        Assert.notNull(request, "No MultipartRequest found");

        MultipartFile multipartFile = request.getFile(excelImport.name());
        Assert.notNull(multipartFile, "No MultipartFile found");

        return processExcelFile(multipartFile, excelImport);
    }

    private List<Object> processExcelFile(MultipartFile file, ExcelImport excelImport) {
        List<Object> resultList = new ArrayList<>();
        try (ExcelReader excelReader = EasyExcel.read(file.getInputStream(), excelImport.dataClass(),
                createListener(resultList, excelImport)).build()) {
            List<ReadSheet> sheets = excelReader.excelExecutor().sheetList();
            for (ReadSheet sheet : sheets) {
                excelReader.read(sheet);
            }
        } catch (Exception e) {
            log.error("Error processing Excel file", e);
            throw new RuntimeException("Failed to process Excel file", e);
        }
        return resultList;
    }

    private DefaultAnalysisEventListener createListener(List<Object> resultList, ExcelImport excelImport) {
        return new DefaultAnalysisEventListener(validator, resultList, excelImport.maxRow(),
                excelImport.minRow(), excelImport.dataClass(), excelImport.validateHead());
    }
}