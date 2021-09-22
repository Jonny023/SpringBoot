package com.example.springbootclickhousenoneid.base;

public enum ResultEnum {

    /**
     * 操作成功
     */
    SUCCESS("00000", "操作成功！"),

    ERROR("99999","系统错误");

    final String code;
    final String msg;

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    private ResultEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
