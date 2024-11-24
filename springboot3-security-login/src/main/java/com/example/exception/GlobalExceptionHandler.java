package com.example.exception;

import cn.hutool.core.util.StrUtil;
import com.example.domain.vo.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

/**
 * @author Jonny
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public R<Object> handleException(Exception e) {
        log.error("服务器内部错误", e);
        return R.error("服务器内部错误");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Object> handleException(MethodArgumentNotValidException e) {
        log.error("参数校验", e);
        String error = e.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining("; "));
        if (StrUtil.isEmpty(error)) {
            error = e.getMessage();
        }
        return R.error(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    public R<Object> handleAuthenticationException(AuthenticationException e) {
        log.error("未登录", e);
        return R.error(HttpStatus.UNAUTHORIZED.value(), "未登录");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public R<Object> handleAccessDeniedException(AccessDeniedException e) {
        log.error("无权访问", e);
        return R.error(HttpStatus.FORBIDDEN.value(), "无权访问");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public R<Object> handleNotFoundException(NoHandlerFoundException e) {
        log.error("访问地址不存在", e);
        return R.error(HttpStatus.NOT_FOUND.value(), "访问地址不存在");
    }
}