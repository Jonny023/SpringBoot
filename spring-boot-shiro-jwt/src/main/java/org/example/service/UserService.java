package org.example.service;

import org.example.domain.entity.User;

public interface UserService {

    /**
     * 通过缓存查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User findUserByCache(String username);
}