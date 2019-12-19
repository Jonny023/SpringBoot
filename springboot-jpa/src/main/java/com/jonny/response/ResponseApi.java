package com.jonny.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseApi<T> {

    private int code;
    private String msg;
    private T data;

    public static<T> ResponseApi<T> success(T data) {
        return new ResponseApi(200, "请求成功", data);
    }

    public static<T> ResponseApi<T> success(String msg) {
        return new ResponseApi(200, msg, null);
    }

    public static<T> ResponseApi<T> success(int code, String msg, T data) {
        return new ResponseApi(code, msg, data);
    }

    public static<T> ResponseApi<T> error(T data) {
        return new ResponseApi(500, "异常", data);
    }

    public static<T> ResponseApi<T> error() {
        return new ResponseApi(500, "异常", null);
    }

    public static<T> ResponseApi<T> error(String msg) {
        return new ResponseApi(500, msg, null);
    }

    public static<T> ResponseApi<T> error(int code, String msg, T data) {
        return new ResponseApi(code, msg, data);
    }

}
