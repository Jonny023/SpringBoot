package com.jonny.springbootjsr303.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    public static final int SUCCESS_CODE = 200;
    public static final int ERROR_CODE = 500;
    public static final int VALID_ERROR_CODE = 501;
    public static final String SUCCESS_MSG = "成功";
    public static final String FAILED_MSG = "失败";

    private int code;
    private String msg;
    private T data;

    public static <T> T ok() {
        return (T) Result.builder().code(SUCCESS_CODE).msg(SUCCESS_MSG).build();
    }

    public static <T> T ok(T data) {
        return (T) Result.builder().code(SUCCESS_CODE).msg(SUCCESS_MSG).data(data).build();
    }

    public static <T> T error(int code, String msg) {
        return (T) Result.builder().code(code).msg(msg).build();
    }

    public static <T> T error(String msg) {
        return (T) Result.builder().code(ERROR_CODE).msg(msg).build();
    }

    public static <T> T validError(String msg) {
        return (T) Result.builder().code(VALID_ERROR_CODE).msg(msg).build();
    }

    public static <T> T error() {
        return (T) Result.builder().code(ERROR_CODE).msg(FAILED_MSG).build();
    }
}
