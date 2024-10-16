package com.example.springbootlogin.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BizException extends RuntimeException {

    private String msg;
    private Throwable cause;

    private BizException(String msg, Throwable cause) {
        this.msg = msg;
        this.cause = cause;
    }

    public BizException(String msg) {
        this.msg = msg;
    }
}