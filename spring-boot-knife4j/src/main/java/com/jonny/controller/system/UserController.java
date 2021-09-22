package com.jonny.controller.system;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jonny.domain.User;
import com.jonny.domain.UserForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {

    @ApiOperation("查询")
    @PostMapping("/get")
    public User list() {
        return new User("", "");
    }

    @ApiOperation("保存")
    @PostMapping("/save")
    @ApiOperationSupport(author = "张三", ignoreParameters = {"password1"})
    public User save(@RequestBody UserForm form) {
        System.out.println();
        return new User("", "");
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @ApiOperationSupport(author = "张三", ignoreParameters = {"password1"})
    public User update(UserForm form) {
        System.out.println();
        return new User("", "");
    }
}
