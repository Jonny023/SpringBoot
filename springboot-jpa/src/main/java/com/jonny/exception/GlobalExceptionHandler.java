package com.jonny.exception;

import com.jonny.response.ResponseApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  全局异常捕获类
 */
@RestControllerAdvice("com.jonny.controller")   //指定异常处理的包名
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseApi exception(Exception exception) {
        logger.error("exception : " + exception);
        return ResponseApi.error(exception.getMessage());
    }

    /**
     * 处理实体字段校验不通过异常
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseApi validationError(MethodArgumentNotValidException ex) {
        logger.error("raised MethodArgumentNotValidException : " + ex);
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        StringBuilder builder = new StringBuilder();

        int i = 0;
        if (fieldErrors !=null && fieldErrors.size() > 0) {
            i = fieldErrors.size();
        }
        for (FieldError error : fieldErrors) {
            if (i > 1) {
                builder.append(error.getDefaultMessage()+"\n");
            } else {
                builder.append(error.getDefaultMessage());
            }
        }
        return ResponseApi.error(builder.toString());
    }
}
