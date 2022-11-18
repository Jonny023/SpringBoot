package org.core.acid.constants;

public enum ResultConstants {

    SUCCESS(200, "成功"),
    NOT_FOUND(404, "无记录"),
    ERROR(500, "失败");

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    ResultConstants(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
