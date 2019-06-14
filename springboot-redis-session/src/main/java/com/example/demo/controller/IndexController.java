package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.RedisCacheStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author Lee
 * @Description
 * @Date 2019年06月14日 21:58
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    @ResponseBody
    public String index() {
        User user = new User();
        user.setId(1L);
        user.setUsername("zhangsan");
        user.setPassword("123456");
        boolean flag = redisCacheStorageService.set("USER-"+user.getId(), user, 80000);
        if (flag) {
            return "已保存";
        } else {
            return "保存失败";
        }
    }

    @Autowired
    private RedisCacheStorageService redisCacheStorageService;
}
