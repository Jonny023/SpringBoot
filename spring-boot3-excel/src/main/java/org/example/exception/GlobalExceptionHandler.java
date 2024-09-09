package org.example.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.vo.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

/**
 * @author admin
 * @description: 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public R<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("全局异常IllegalArgumentException:", ex);
        return R.error(error(ex));
    }

    @ExceptionHandler(Exception.class)
    public R<String> exception(Exception ex) {
        log.error("全局异常Exception:", ex);
        return R.error(error(ex));
    }

    private String error(Throwable e) {
        return Optional.ofNullable(e.getMessage()).orElse("服务器异常");
    }
}