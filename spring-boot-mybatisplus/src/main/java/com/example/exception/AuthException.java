package com.example.exception;

import com.example.base.ResultEnum;

public class AuthException extends RuntimeException {

    private ResultEnum resultEnum;

    public String getCode() {
        return resultEnum.getCode();
    }

    public AuthException() {
        super();
    }

    public AuthException(ResultEnum resultEnum) {
        super(resultEnum.toString());
        this.resultEnum = resultEnum;
    }
}
