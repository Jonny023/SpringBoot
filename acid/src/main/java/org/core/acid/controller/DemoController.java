package org.core.acid.controller;

import org.core.acid.base.Page;
import org.core.acid.entity.User;
import org.core.acid.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    /**
     *  列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Page<User> list(Page page) {
        return userService.list(page);
    }
}
