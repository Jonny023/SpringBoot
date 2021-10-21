package org.jonny.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jonny.service.IUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    protected IUserService userService;

    @GetMapping("/{id}")
    @RequiresPermissions("user:get")
    public Object test(@PathVariable("id") Long id) {
        return userService.getById(id);
    }

    @GetMapping("/list")
    @RequiresPermissions("user:list")
    public Object list() {
        return userService.list();
    }
}