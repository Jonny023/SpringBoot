package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.RedisCacheStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;

/**
 * @Author Lee
 * @Description
 * @Date 2019年06月14日 21:58
 */
@Controller
@SessionAttributes("user")
public class IndexController {

    @ModelAttribute("user")
    public User populateForm() {
        return null; // populates form for the first time if its null
    }

    @RequestMapping("/")
    @ResponseBody
    public String index(HttpSession session) {
        User user = new User();
        user.setId(1L);
        user.setUsername("zhangsan");
        user.setPassword("123456");
        boolean flag = redisCacheStorageService.set("USER-"+user.getId(), user, 80000);
        session.setAttribute("user", user);
        if (flag) {
            return "已保存";
        } else {
            return "保存失败";
        }
    }

    @RequestMapping("/session")
    @ResponseBody
    public String t(@ModelAttribute("user") User user) {
        try {
            return user.toString();
        } catch (Exception e) {
            return "user对象为空";
        }
    }

    @RequestMapping("/session1")
    @ResponseBody
    public String t1(@SessionAttribute("user") User user) {
        return user.toString();
    }

    @Autowired
    private RedisCacheStorageService redisCacheStorageService;
}
