package org.example.excel.handler.imports;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.StringUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * 默认的数据解析监听器
 *
 * @author Jonny
 */
@Slf4j
public class DefaultAnalysisEventListener extends AnalysisEventListener<Object> {

    private final int maxRow;
    private final int minRow;
    private final boolean validateHead;
    private final Validator validator;
    private final List<Object> list;
    private final Class<?> dataClass;

    public DefaultAnalysisEventListener(Validator validator, List<Object> list, int maxRow, int minRow, Class<?> dataClass, boolean validateHead) {
        this.maxRow = maxRow;
        this.minRow = minRow;
        this.validator = validator;
        this.list = list;
        this.dataClass = dataClass;
        this.validateHead = validateHead;
    }

    @Override
    public void invoke(Object data, AnalysisContext context) {
        validateRowCount();
        validateData(data);
        list.add(data);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        if (validateHead) {
            validateExcelHeader(headMap);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        validateMinimumRowCount();
    }

    private void validateRowCount() {
        if (maxRow > 0 && list.size() >= maxRow) {
            throw new IllegalArgumentException("数据行数超过最大行数: " + maxRow);
        }
    }

    private void validateData(Object data) {
        Set<ConstraintViolation<Object>> violations = validator.validate(data);
        if (!violations.isEmpty()) {
            String errorMessage = violations.iterator().next().getMessage();
            throw new IllegalArgumentException("数据验证失败: " + errorMessage);
        }
    }

    private void validateExcelHeader(Map<Integer, String> headMap) {
        Stream.of(dataClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ExcelProperty.class))
                .forEach(field -> validateHeaderField(field, headMap));
    }

    /**
     * 校验如果Excel表头与注解不匹配，则抛出异常
     *
     * @param field   字段
     * @param headMap 单元格数据
     */
    private void validateHeaderField(Field field, Map<Integer, String> headMap) {
        ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
        int index = annotation.index();
        String expectedValue = annotation.value()[0];
        String actualValue = headMap.get(index);

        if (StringUtils.isBlank(actualValue) || !actualValue.equals(expectedValue)) {
            throw new IllegalArgumentException("Excel表头不合法: 期望 '" + expectedValue + "', 实际 '" + actualValue + "'");
        }
    }

    private void validateMinimumRowCount() {
        if (minRow > 0 && list.size() < minRow) {
            throw new IllegalArgumentException("数据行数小于最小行数: " + minRow);
        }
    }
}