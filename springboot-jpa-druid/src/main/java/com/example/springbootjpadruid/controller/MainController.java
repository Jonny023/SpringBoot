package com.example.springbootjpadruid.controller;

import com.example.springbootjpadruid.domain.entity.second.Test;
import com.example.springbootjpadruid.repository.primary.UserRepository;
import com.example.springbootjpadruid.repository.second.TestRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MainController {

    @Resource
    private UserRepository userRepository;
    @Resource
    private TestRepository testRepository;


    @GetMapping("/list/1")
    public Object list1() {
        return userRepository.findAll();
    }

    @GetMapping("/list/2")
    public Object list2() {
        return testRepository.findAll();
    }

    @PostMapping("/addTest")
    @Transactional(rollbackFor = Exception.class)
    public Object add(@RequestBody Test test) {
        return testRepository.save(test);
    }
}
