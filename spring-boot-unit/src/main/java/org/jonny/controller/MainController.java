package org.jonny.controller;

import org.jonny.service.IUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: e-lijing6
 * @description:
 * @date:created in 2021/5/30 15:42
 * @modificed by:
 */
@RestController
public class MainController {

    @Resource
    private IUserService userService;

    @GetMapping("/hello")
    public String hello() {
        return userService.run();
    }
}
