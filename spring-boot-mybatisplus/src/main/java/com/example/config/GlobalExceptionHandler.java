package com.example.config;

import com.example.exception.AuthException;
import com.example.base.ResultEnum;
import com.example.base.ResultVO;
import com.example.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public static final String DATA_VALID_ERROR = "数据校验异常 {}";
    public static final String BUSINESS_ERROR = "业务类异常，错误码:{} {}";

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVO handlerException(HttpServletRequest request, Exception ex){
        logger.error("{}",ex);
        return ResultVO.error(ResultEnum.ERROR.getCode(),ResultEnum.ERROR.getMsg());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVO bindException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        logger.error(DATA_VALID_ERROR, e);
        return ResultVO.error(ResultEnum.DATA_VALID_ERROR.getCode(), Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(BizException.class)
    public ResultVO bizException(BizException e) {
        logger.error(BUSINESS_ERROR, e.getCode(), e);
        return ResultVO.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(AuthException.class)
    public ResultVO authException(AuthException e) {
        logger.error(BUSINESS_ERROR, e.getCode(), e);
        return ResultVO.error(e.getCode(), e.getMessage());
    }

}
