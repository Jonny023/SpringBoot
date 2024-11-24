package com.example.security.handler.impl;

import com.example.domain.entity.User;
import com.example.domain.vo.common.R;
import com.example.security.handler.AuthenticationHandler;
import com.example.security.request.AuthRequest;
import com.example.security.request.EmailPasswordRequest;
import com.example.service.UserService;
import com.example.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EmailPasswordAuthHandler extends AuthenticationHandler {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserService userService;

    @Override
    public R<Object> authenticate(AuthRequest request) {
        EmailPasswordRequest req = (EmailPasswordRequest) request;
        User user = userService.findByEmail(req.getEmail());
        if (user != null && passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            String token = JwtUtil.createToken(user.getId(), user.getUsername());
            return R.ok(token);
        }
        return R.error("邮箱或密码错误");
    }
}