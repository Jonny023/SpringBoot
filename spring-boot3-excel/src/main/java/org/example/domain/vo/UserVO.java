package org.example.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ExcelIgnoreUnannotated
public class UserVO {

    /**
     * 主键
     */
    @ExcelProperty(value = "主键", index = 0)
    private Long id;

    /**
     * 姓名
     */
    @ExcelProperty(value = "姓名", index = 1)
    private String name;

    /**
     * 地址
     */
    @ExcelProperty(value = "地址", index = 2)
    private String address;
}