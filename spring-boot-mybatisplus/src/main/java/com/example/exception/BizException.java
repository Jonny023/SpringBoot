package com.example.exception;

import com.example.base.ResultEnum;

/**
 *  自定义业务类异常
 */
public class BizException extends RuntimeException {

    private ResultEnum resultEnum;

    public String getCode() {
        return resultEnum.getCode();
    }

    public BizException() {
        super();
    }

    public BizException(ResultEnum resultEnum) {
        super(resultEnum.toString());
        this.resultEnum = resultEnum;
    }
}
