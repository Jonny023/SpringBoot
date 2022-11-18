package com.example.springbootshiro.domain.enums;

public enum ResponseEnum {

    OK(0, "成功"),
    ERROR(1, "失败"),
    UNAUTHORIZED(10001, "未授权"),
    TOKEN_UNAVAILABLE(10002, "无效token"),
    TOKEN_EXPIRE(10002, "token已过期")
    ;

    private int code;
    private String msg;

    ResponseEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
