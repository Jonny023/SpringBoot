package com.example.springbootlogin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.example.springbootlogin.domain.entity.User;
import com.example.springbootlogin.exception.BizException;
import com.example.springbootlogin.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    /**
     * 前台登录
     *
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        if (Objects.isNull(user)) {
            throw new BizException("用户不能为空");
        }
        if (!StrUtil.equals("demo", user.getUsername())) {
            throw new BizException("用户不存在");
        }
        if (!StrUtil.equals("123456", user.getPassword())) {
            throw new BizException("密码不正确");
        }
        user.setId(1L);
        return user;
    }

    /**
     * 后台登录
     *
     * @param user
     * @return
     */
    @Override
    public User bgLogin(User user) {
        if (Objects.isNull(user)) {
            throw new BizException("用户不能为空");
        }
        if (!StrUtil.equals("admin", user.getUsername())) {
            throw new BizException("用户不存在");
        }
        if (!StrUtil.equals("123456", user.getPassword())) {
            throw new BizException("密码不正确");
        }
        user.setId(1L);
        return user;
    }
}