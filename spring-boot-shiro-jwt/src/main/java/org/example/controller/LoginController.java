package org.example.controller;

import org.example.domain.vo.LoginVO;
import org.example.domain.vo.R;
import org.example.shiro.util.ContextUtil;
import org.example.shiro.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/auth")
    public R<String> auth(@RequestBody LoginVO params) {
        Map<String, Object> chaim = new HashMap<>();
        chaim.put("username", params.getUsername());
        String jwtToken = JwtUtil.createToken(chaim);
        return R.ok(jwtToken);
    }

    @PostMapping("/test")
    public String test() {
        // 通过工具类的ThreadLocal传递全局参数，执行完成后会调用remove自动移除
        ContextUtil.get().put("type", "120");
        log.info("当前登录用户：{},{}", ContextUtil.userId(), ContextUtil.username());
        System.out.println(ContextUtil.get());
        return "hello";
    }
}