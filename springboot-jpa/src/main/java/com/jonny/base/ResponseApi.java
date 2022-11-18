package com.jonny.base;

import lombok.Builder;
import lombok.Data;

/**
* lombok泛型实例不能通过new创建，需要通过Class.builder()构建
*/
@Data
@Builder
public class ResponseApi<T> {

    private int code;
    private String msg;
    private T data;

//    @Tolerate
//    public ResponseApi(int code, String msg, T data) {
//        this.code = code;
//        this.msg = msg;
//        this.data = data;
//    }

    public static<T> ResponseApi<T> success(T data) {
        return ResponseApi.<T>builder().code(200).msg("请求成功").data(data).build();
    }

    public static<T> ResponseApi<T> success(String msg) {
        return ResponseApi.<T>builder().code(200).msg(msg).data(null).build();
    }

    public static<T> ResponseApi<T> success(int code, String msg, T data) {
        return ResponseApi.<T>builder().code(code).msg(msg).data(data).build();

    }

    public static<T> ResponseApi<T> error(T data) {
        return ResponseApi.<T>builder().code(500).msg("异常").data(data).build();
    }

    public static<T> ResponseApi<T> error() {
        return ResponseApi.<T>builder().code(500).msg("异常").data(null).build();
    }

    public static<T> ResponseApi<T> error(String msg) {
        return ResponseApi.<T>builder().code(500).msg(msg).data(null).build();
    }

    public static<T> ResponseApi<T> error(int code, String msg, T data) {
        return ResponseApi.<T>builder().code(code).msg(msg).data(data).build();
    }

}
