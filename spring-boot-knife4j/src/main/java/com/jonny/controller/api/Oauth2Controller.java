package com.jonny.controller.api;

import com.jonny.domain.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "认证")
@RestController
@RequestMapping("/oauth")
public class Oauth2Controller {

    @ApiOperation("查询")
    @PostMapping("/list")
    public User list() {
        return new User("admin", "admin");
    }
}
