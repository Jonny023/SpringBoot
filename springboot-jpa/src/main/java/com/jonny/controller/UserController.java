package com.jonny.controller;

import com.jonny.entity.User;
import com.jonny.response.ResponseApi;
import com.jonny.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @ResponseBody
    @RequestMapping("/list")
    public ResponseApi list() {
        return userService.list();
    }

    @ResponseBody
    @RequestMapping("/save")
    public ResponseApi save(@RequestBody @Valid User user) {
        System.out.println(user);
        return userService.save(user);
    }

    @ResponseBody
    @RequestMapping("/update")
    public ResponseApi update(@RequestBody @Valid User user) {
        return userService.update(user);
    }

    @ResponseBody
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseApi delete(@PathVariable("id") Long id) {
        return userService.delete(id);
    }

    @ResponseBody
    @RequestMapping(value = "/findByUsername", method = RequestMethod.POST)
    public ResponseApi findByUseranme(String username) {
        return userService.findByUsername(username);
    }

    @ResponseBody
    @RequestMapping(value = "/findByUsernameAndName", method = RequestMethod.POST)
    public ResponseApi findByUsernameAndName(String username, String name) {
        return userService.findByUsernameAndName(username, name);
    }

    @ResponseBody
    @RequestMapping(value = "/queryUsernames", method = RequestMethod.POST)
    public ResponseApi queryUsernames() {
        return userService.queryUsernames();
    }

    @ResponseBody
    @RequestMapping(value = "/queryColByUsernameAndName", method = RequestMethod.POST)
    public ResponseApi queryColByUsernameAndName(String username, String name) {
        return userService.queryColByUsernameAndName(username, name);
    }

}
