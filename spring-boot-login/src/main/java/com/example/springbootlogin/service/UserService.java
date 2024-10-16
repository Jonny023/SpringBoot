package com.example.springbootlogin.service;

import com.example.springbootlogin.domain.entity.User;

public interface UserService {

    User login(User user);

    User bgLogin(User user);
}
