package com.example.service;

import com.example.domain.entity.User;

public interface UserService {

    User findByEmail(String email);

    User findByPhone(String phone);

    User findByUsername(String username);
}