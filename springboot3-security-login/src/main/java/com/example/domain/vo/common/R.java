package com.example.domain.vo.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 响应类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class R<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 8899461877256816826L;

    /**
     * 成功
     */
    private static final int OK = 200;
    private static final String OK_MSG = "成功";

    /**
     * 失败
     */
    private static final int ERROR = 500;
    private static final String ERR_MSG = "失败";

    /**
     * 状态码 200成功，其他失败
     */
    private int code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    @SuppressWarnings("unchecked")
    private static <T> RBuilder<T> buildOk() {
        return (RBuilder<T>) R.builder().code(OK).msg(OK_MSG);
    }

    @SuppressWarnings("unchecked")
    private static <T> RBuilder<T> buildError() {
        return (RBuilder<T>) R.builder().code(ERROR).msg(ERR_MSG);
    }

    public static R<Object> ok() {
        return buildOk().build();
    }

    @SuppressWarnings("unchecked")
    public static <T> R<Object> ok(T data) {
        return buildOk().data(data).build();
    }

    public static R<Object> error() {
        return buildError().build();
    }

    public static R<Object> error(String msg) {
        return buildError().msg(msg).build();
    }

    public static R<Object> error(int code, String msg) {
        return buildError().code(code).msg(msg).build();
    }
}