package com.example.springbootshiro.configuration.shiro.exception;

import com.example.springbootshiro.domain.enums.ResponseEnum;
import com.example.springbootshiro.domain.response.R;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(1)
public class ShiroExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ShiroExceptionAdvice.class);

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({AuthenticationException.class, UnknownAccountException.class, UnauthenticatedException.class, IncorrectCredentialsException.class})
    public R unauthorized(Exception exception) {
        logger.warn(exception.getMessage(), exception);
        return R.error(ResponseEnum.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public R unauthorized1(UnauthorizedException exception) {
        logger.warn(exception.getMessage(), exception);
        return R.error(ResponseEnum.UNAUTHORIZED);
    }
}
