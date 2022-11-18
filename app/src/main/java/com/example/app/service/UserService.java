package com.example.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.app.domain.entity.User;

import java.util.List;

public interface UserService extends IService<User> {

    List<User> list();
}
