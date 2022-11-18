package com.jonny.springbootjsr303.exception;

import com.jonny.springbootjsr303.base.Result;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(Exception e){
        if (e instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
            //多个错误，取第一个
            FieldError error = fieldErrors.get(0);
            String msg = error.getDefaultMessage();
            return Result.validError(msg);
        }else {
            return Result.error(e.getMessage());
        }
    }
}
