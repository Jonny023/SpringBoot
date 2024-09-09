package org.example.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class R<T> implements Serializable {

    private static final int SUCCESS_CODE = 200;
    private static final int ERROR_CODE = 500;

    @Serial
    private static final long serialVersionUID = -1454364708340875414L;

    /**
     * 200 成功
     */
    private int code;

    /**
     * 状态信息 true成功
     */
    private boolean success;

    /**
     * 返回信息
     */
    private T data;

    /**
     * 消息
     */
    private String message;

    public R() {
        this.code = SUCCESS_CODE;
    }

    public R(T data) {
        this.code = SUCCESS_CODE;
        this.data = data;
    }

    public R(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> R<T> ok(T data) {
        return new R<>(data);
    }

    public boolean isSuccess() {
        return SUCCESS_CODE == code;
    }

    public static <T> R<T> error(int code, String message) {
        return new R<>(code, message);
    }

    public static <T> R<T> error(String message) {
        return new R<>(ERROR_CODE, message);
    }
}
