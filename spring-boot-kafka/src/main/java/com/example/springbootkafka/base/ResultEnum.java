package com.example.springbootkafka.base;

public enum ResultEnum {

    /**
     * 操作成功
     */
    SUCCESS("00000", "操作成功！"),

    TIMEOUT("10001", "超时"),
    EXCEPTION("10002", "异常"),

    ERROR("99999","系统错误")
    ;

    final String code;
    final String msg;

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    ResultEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
