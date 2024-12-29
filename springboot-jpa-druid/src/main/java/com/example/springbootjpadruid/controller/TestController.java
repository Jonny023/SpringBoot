package com.example.springbootjpadruid.controller;

import com.example.springbootjpadruid.domain.common.PageVO;
import com.example.springbootjpadruid.domain.entity.primary.User;
import com.example.springbootjpadruid.domain.query.UserQuery;
import com.example.springbootjpadruid.domain.vo.UserVO;
import com.example.springbootjpadruid.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private UserService service;

    @RequestMapping("/init")
    public String init() {
        String[] address = {"北京", "上海", "广州", "深圳", "重庆"};
        List<User> users = new ArrayList<>();
        IntStream.range(1, 30).forEach(i -> {
            User user = new User();
            user.setUsername("user" + i);
            user.setAge(i);
            user.setAddress(address[i % 5]);
            users.add(user);
        });
        return service.init(users);
    }

    @GetMapping("/user")
    public User getUser() {
        return service.getUser();
    }

    @GetMapping("/list")
    PageVO<UserVO> listPage(UserQuery param) {
        return service.listPage(param);
    }

    @GetMapping("/listNativeSql")
    PageVO<UserVO> listNativeSql(UserQuery param) {
        return service.nativeListPage(param);
    }

    @GetMapping("/listPageByJpaUtil")
    PageVO<UserVO> listPageByJpaUtil(UserQuery param) {
        return service.listPageByJpaUtil(param);
    }
}