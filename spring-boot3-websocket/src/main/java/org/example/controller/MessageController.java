package org.example.controller;

import org.example.utils.RedisUtil;
import org.example.websocket.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private Environment environment;

    @PostMapping("/msg/send")
    public String sendMsg(@RequestParam("msg") String msg, @RequestParam("username") String username) {
        // 推送站内信webSocket
        redisUtil.publish(WebSocketServer.TOPIC_PREFIX + username, msg);
        return "ok" + environment.getProperty("server.port");
    }
}