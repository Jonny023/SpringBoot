package com.example.springbootlogin.controller.bg;

import cn.hutool.core.date.DateUtil;
import com.example.springbootlogin.domain.context.UserContext;
import com.example.springbootlogin.domain.entity.User;
import com.example.springbootlogin.domain.vo.R;
import com.example.springbootlogin.service.UserService;
import com.example.springbootlogin.utils.JwtUtil;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.springbootlogin.constant.Const.EXPIRATION_TIME;

@Slf4j
@RestController
@RequestMapping("/bg")
public class BgLoginController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public R login(@RequestBody User user) {
        Map<String, Object> map = Maps.newHashMap();
        User login = userService.bgLogin(user);
        long expireTime = DateUtil.current() + EXPIRATION_TIME;
        map.put("uid", login.getId());
        map.put("username", login.getUsername());
        map.put("exp", expireTime);
        String token = JwtUtil.create(map);
        return R.ok(token);
    }

    @GetMapping("/currentUser")
    public R getInfo() {
        log.info("当前登录用户：{}", UserContext.username());
        return R.ok();
    }
}
