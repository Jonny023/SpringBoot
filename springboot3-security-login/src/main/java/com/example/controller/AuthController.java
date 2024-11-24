package com.example.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.example.domain.vo.common.R;
import com.example.security.enums.LoginEnum;
import com.example.security.handler.AuthenticationHandler;
import com.example.security.request.EmailPasswordRequest;
import com.example.security.request.PhoneCodeRequest;
import com.example.security.request.UsernamePasswordRequest;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * 用户名+密码登录
     *
     * @param request 请求参数
     * @return token
     */
    @PostMapping("/login")
    public R<Object> login(@RequestBody @Valid UsernamePasswordRequest request) {
        AuthenticationHandler handler = getHandler(LoginEnum.USERNAME_PASSWORD);
        return handler.doLogin(request);
    }

    /**
     * 手机号+验证码登录
     *
     * @param request 请求参数
     * @return token
     */
    @PostMapping("/phone")
    public R<Object> login(@RequestBody @Valid PhoneCodeRequest request) {
        AuthenticationHandler handler = getHandler(LoginEnum.PHONE_CODE);
        return handler.doLogin(request);
    }

    /**
     * 邮箱+密码登录
     *
     * @param request 请求参数
     * @return token
     */
    @PostMapping("/email")
    public R<Object> login(@RequestBody @Valid EmailPasswordRequest request) {
        AuthenticationHandler handler = getHandler(LoginEnum.EMAIL_PASSWORD);
        return handler.doLogin(request);
    }

    private AuthenticationHandler getHandler(LoginEnum type) {
        return (AuthenticationHandler) SpringUtil.getBean(type.getClazz());
    }
}