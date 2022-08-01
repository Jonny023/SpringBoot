package com.example.pay.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 0-native(二维码)
 * 1-h5
 */
public enum BooleanEnum {
    /**
     * 支付类型
     */
    TRUE(1, "是"),
    FALSE(0, "否");

    private int value;

    private String text;

    BooleanEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * mybatis Mapper中的方法参数如果为枚举的话, 会调用toString()方法获取枚举值, 用于ognl表达式判断
     */
    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    @JsonValue
    public int value() {
        return this.value;
    }

    public String getText() {
        return text;
    }

    @JsonCreator
    public static BooleanEnum getItem(Integer code) {
        return getEnum(code);
    }

    private static BooleanEnum getEnum(Integer val) {
        if (val == null) {
            return null;
        }
        BooleanEnum[] values = BooleanEnum.values();
        for (BooleanEnum e : values) {
            if (e.value == val) {
                return e;
            }
        }
        return null;
    }

}
