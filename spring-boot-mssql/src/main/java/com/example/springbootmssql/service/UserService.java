package com.example.springbootmssql.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springbootmssql.entity.User;
import com.github.pagehelper.PageInfo;

public interface UserService extends IService<User> {

    PageInfo<User> listPage(PageDTO pageDTO);
}
