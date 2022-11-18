package com.example.springbootshiro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootshiro.configuration.shiro.utils.JwtUtil;
import com.example.springbootshiro.domain.dto.UserDTO;
import com.example.springbootshiro.domain.entity.User;
import com.example.springbootshiro.domain.form.LoginForm;
import com.example.springbootshiro.domain.response.R;
import com.example.springbootshiro.expection.AuthException;
import com.example.springbootshiro.mapper.UserMapper;
import com.example.springbootshiro.service.UserService;
import ml.ytooo.redis.single.JedisClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private JedisClient jedisClient;

    @Override
    public R login(LoginForm form) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(form.getUsername());
        Object token = null;
        try {
            token = JwtUtil.createToken(userDTO);
        } catch (Exception e) {
            throw new AuthException("登录失败", e);
        }
        jedisClient.getJedis().setex(form.getUsername(), (long) 30 * 60, String.valueOf(token));
        return R.ok(token);
    }
}