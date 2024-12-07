package com.example.domain.vo;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataDemo {

    @ExcelProperty("编号")
    private String no;

    @ExcelProperty("学生")
    private String stuName;

    @ExcelProperty(value = {"科目", "语文", "分数"})
    private BigDecimal ywScore;

    @ExcelProperty(value = {"科目", "语文", "占比"})
    private BigDecimal ywRatio;

    @ExcelProperty(value = {"科目", "数学", "分数"})
    private BigDecimal sxScore;

    @ExcelProperty(value = {"科目", "数学", "占比"})
    private BigDecimal sxRatio;

    @ExcelProperty("总成绩")
    private BigDecimal totalScore;

    @ExcelProperty("平均成绩")
    private BigDecimal avgScore;

    public void calc() {
        MathContext mathContext = new MathContext(2, RoundingMode.HALF_UP);
        // 计算总成绩
        BigDecimal total = NumberUtil.add(ywScore, sxScore);
        totalScore = total;
        avgScore = NumberUtil.div(total, 2).round(mathContext);
        ywRatio = NumberUtil.div(ywScore, total).round(mathContext);
        sxRatio = NumberUtil.div(sxScore, total).round(mathContext);
    }
}