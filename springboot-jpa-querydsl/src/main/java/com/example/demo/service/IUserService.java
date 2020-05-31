package com.example.demo.service;

import com.example.demo.entity.User;

public interface IUserService {

    User save(User user);

    User update(User user);

    User delete();

    User get(String username);

}
