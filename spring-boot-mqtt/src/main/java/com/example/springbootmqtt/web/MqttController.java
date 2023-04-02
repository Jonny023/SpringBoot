package com.example.springbootmqtt.web;

import com.example.springbootmqtt.service.MqttService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mqtt")
public class MqttController {

    @Autowired
    private MqttService mqttService;

    @PostMapping("/send")
    public void sendMessage(@RequestBody String message) throws MqttException {
        mqttService.sendMessage(message);
    }

    @GetMapping("/receive")
    public String receiveMessage() {
        // TODO: 返回接收到的消息
        return null;
    }
}