package com.example.app.controller;

import com.example.app.config.DBContextHolder;
import com.example.app.domain.entity.User;
import com.example.app.service.DBChangeService;
import com.example.app.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController {

    @Resource
    private DBChangeService dbChangeServiceImpl;
    @Resource
    UserService userService;

    /**
     * 查询所有
     * @return
     */
    @GetMapping("/test/{id}")
    public  String test(@PathVariable("id") Long id) throws Exception {

        //切换到数据库dbtest2
        dbChangeServiceImpl.changeDb(id);
        List<User> userList= userService.list();
        System.out.println(userList.toString());

        //再切换到数据库dbtest3
        dbChangeServiceImpl.changeDb(id);
        List<User> userList3= userService.list();
        System.out.println(userList3.toString());

        //切回主数据源
        DBContextHolder.clearDataSource();
        return "ok";
    }

}