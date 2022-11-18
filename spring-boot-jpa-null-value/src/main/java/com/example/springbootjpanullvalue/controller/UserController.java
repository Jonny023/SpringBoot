package com.example.springbootjpanullvalue.controller;

import com.example.springbootjpanullvalue.entity.User;
import com.example.springbootjpanullvalue.repository.UserRepository;
import java.util.List;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserRepository userRepository;

    @GetMapping("/save")
    @Transactional(rollbackOn = Exception.class)
    public String save() {
        User user = new User().setId(1L).setUsername("admin").setAddr("China1");
        userRepository.save(user);
        return "save success";
    }

    @GetMapping("/save1")
    @Transactional(rollbackOn = Exception.class)
    public String save1() {
        User user = new User().setId(1L).setAddr("China1");
        userRepository.save(user);
        return "save success";
    }

    @PutMapping("/update")
    @Transactional(rollbackOn = Exception.class)
    public String update(@RequestBody User user) {
        userRepository.save(user);
        return "save success";
    }

    @GetMapping("/list")
    public List<User> list() {
        return userRepository.findAll();
    }

}
