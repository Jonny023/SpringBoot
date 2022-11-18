package org.core.acid.exception;

import lombok.extern.slf4j.Slf4j;
import org.core.acid.base.R;
import org.core.acid.constants.ResultConstants;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *  全局异常拦截
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 拦截所有运行时的全局异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public String runtimeException(RuntimeException e) {
        log.error(e.getMessage(), e);
        R r = new R(ResultConstants.ERROR.getCode(), ResultConstants.ERROR.getMsg());
        return r.toString();
    }

    /**
     * 系统异常捕获处理
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String exception(Exception e) {
        log.error(e.getMessage(), e);
        R r = new R(ResultConstants.ERROR.getCode(), ResultConstants.ERROR.getMsg());
        return r.toString();
    }
}
