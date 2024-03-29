package com.example.util;

import java.math.BigDecimal;

/**
 * 任务进度计算工具
 */
public final class ProgressUtil {

    private ProgressUtil() {
    }

    /**
     * @param molecular   分子
     * @param denominator 分母
     * @return
     */
    public static String calc(int molecular, int denominator) {
        if (denominator == 0) {
            return "0%";
        }
        BigDecimal result = BigDecimal.valueOf(molecular).divide(BigDecimal.valueOf(denominator), 6, BigDecimal.ROUND_HALF_UP)
        .multiply(BigDecimal.valueOf(100))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        return String.format("%s%%", result.stripTrailingZeros().toPlainString());
    }
}
