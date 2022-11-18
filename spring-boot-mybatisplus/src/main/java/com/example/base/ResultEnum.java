package com.example.base;

public enum ResultEnum {

    SUCCESS("00000", "操作成功！"),
    ERROR("99999","系统错误"),

    //签名类
    NEED_CERTIFICATION("10011", "请授权后访问"),

    //业务相关
    DATA_VALID_ERROR("B10009", "数据校验异常！"),
    DATE_VALID_ERROR("B10011", "结束时间必须大于开始时间！")

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

    @Override
    public String toString() {
        return this.msg;
    }
}
