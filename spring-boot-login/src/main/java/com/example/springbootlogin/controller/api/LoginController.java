package com.example.springbootlogin.controller.api;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWTUtil;
import com.example.springbootlogin.constant.Const;
import com.example.springbootlogin.domain.entity.User;
import com.example.springbootlogin.domain.vo.R;
import com.example.springbootlogin.service.UserService;
import com.example.springbootlogin.utils.JwtUtil;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.example.springbootlogin.constant.Const.EXPIRATION_TIME;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Resource
    private UserService userService;

    @PostMapping
    public R login(@RequestBody User user) {
        Map<String, Object> map = Maps.newHashMap();
        User login = userService.login(user);
        long expireTime = DateUtil.current() + EXPIRATION_TIME;
        map.put("uid", login.getId());
        map.put("username", login.getUsername());
        map.put("exp", expireTime);
        String token = JwtUtil.create(map);
        return R.ok(token);
    }
}
