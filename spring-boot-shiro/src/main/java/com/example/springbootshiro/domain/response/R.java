package com.example.springbootshiro.domain.response;

import com.example.springbootshiro.domain.enums.ResponseEnum;
import io.swagger.annotations.ApiModelProperty;

public class R<T> {

    @ApiModelProperty("状态码：0-成功,其他-失败")
    private int code;

    @ApiModelProperty("提示消息")
    private String msg;

    @ApiModelProperty("返回数据")
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> R<T> ok() {
        return new R<T>(ResponseEnum.OK.getCode(), ResponseEnum.OK.getMsg(), null);
    }

    public static <T> R<T> ok(T data) {
        return new R<T>(ResponseEnum.OK.getCode(), ResponseEnum.OK.getMsg(), data);
    }

    public static <T> R<T> ok(String msg) {
        return new R<T>(ResponseEnum.OK.getCode(), msg, null);
    }

    public static <T> R<T> ok(int code, String msg, T data) {
        return new R<T>(code, msg, data);
    }

    public static <T> R<T> error() {
        return new R<T>(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMsg(), null);
    }

    public static <T> R<T> error(ResponseEnum responseEnum) {
        return new R<T>(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMsg(), null);
    }

    public static <T> R<T> error(int code, String msg) {
        return new R<T>(code, msg, null);
    }
}
