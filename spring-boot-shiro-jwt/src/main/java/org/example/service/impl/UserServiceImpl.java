package org.example.service.impl;

import org.example.domain.entity.User;
import org.example.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    /**
     * 模拟数据库查询用户（先从缓存读取，缓存没有，再从数据库读取）
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public User findUserByCache(String username) {
        return User.builder().id(1L).username("admin1").password("123").build();
    }
}