package com.example.springbootshiro.controller;

import com.example.springbootshiro.domain.form.LoginForm;
import com.example.springbootshiro.domain.response.R;
import com.example.springbootshiro.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ml.ytooo.security.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "用户")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation("登录")
    @PostMapping("/login")
    public R login(LoginForm form) {
        return userService.login(form);
    }

    @ApiOperation("查询")
    @PostMapping("/get")
    public R get(HttpServletResponse response) {
        return R.ok(JwtUtil.verifyJavaWebToken(response.getHeader("Authorization")));
    }
}