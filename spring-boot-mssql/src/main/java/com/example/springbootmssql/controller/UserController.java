package com.example.springbootmssql.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.example.springbootmssql.entity.User;
import com.example.springbootmssql.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/listPage")
    public PageInfo<User> listPage(@RequestBody PageDTO pageDTO) {
        return userService.listPage(pageDTO);
    }

    @PostMapping("/list")
    public List<User> list() {
        return userService.list();
    }

    @PostMapping("/save")
    public User save() {
        User user = User.builder().age(20).name("张三").build();
        userService.save(user);
        return user;
    }
}
