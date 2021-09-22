package org.core.acid.base;

import lombok.Builder;
import lombok.Data;
import org.core.acid.constants.ResultConstants;

@Data
@Builder
public class R<T> {

    private int code;
    private String msg;
    private T data;

    public R(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public R(String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> R<T> success() {
        return new R<T>(ResultConstants.SUCCESS.getCode(), ResultConstants.SUCCESS.getMsg());
    }

    public static <T> R<T> success(T data) {
        return new R<T>(ResultConstants.SUCCESS.getCode(), ResultConstants.SUCCESS.getMsg(), data);
    }

    public static <T> R<T> success(String msg, T data) {
        return new R<T>(ResultConstants.SUCCESS.getCode(), msg, data);
    }

    public static <T> R<T> error(int code, String msg) {
        return new R<T>(code, msg);
    }

    public static <T> R<T> error(String msg) {
        return new R<T>(ResultConstants.ERROR.getCode(), msg);
    }

    public static <T> R<T> notFound(String msg) {
        return new R<T>(ResultConstants.NOT_FOUND.getCode(), msg);
    }

    public static <T> R<T> notFound() {
        return new R<T>(ResultConstants.NOT_FOUND.getCode(), ResultConstants.NOT_FOUND.getMsg());
    }

}
