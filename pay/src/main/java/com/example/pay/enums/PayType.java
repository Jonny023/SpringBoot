package com.example.pay.enums;

public enum PayType {

    /**
     * 支付方式
     */
    ALI_PAY(1, "支付宝支付"),

    WECHAT_PAY(2, "微信支付"),

    UNION_PAY(3, "银联云闪付支付");

    private Integer type;
    private String desc;

    PayType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

}
