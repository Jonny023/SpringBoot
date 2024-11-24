package com.example.security.handler.impl;

import com.example.domain.entity.User;
import com.example.domain.vo.common.R;
import com.example.security.handler.AuthenticationHandler;
import com.example.security.request.AuthRequest;
import com.example.security.request.UsernamePasswordRequest;
import com.example.service.UserService;
import com.example.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsernamePasswordAuthHandler extends AuthenticationHandler {

    @Resource
    private UserService userService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public R<Object> authenticate(AuthRequest request) {
        UsernamePasswordRequest req = (UsernamePasswordRequest) request;
        User user = userService.findByUsername(req.getUsername());
        if (user != null && passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            String token = JwtUtil.createToken(user.getId(), user.getUsername());
            return R.ok(token);
        }
        return R.error("用户名或密码错误");
    }
}