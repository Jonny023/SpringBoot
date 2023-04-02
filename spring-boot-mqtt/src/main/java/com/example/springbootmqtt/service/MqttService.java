package com.example.springbootmqtt.service;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.example.springbootmqtt.config.MqttClientConfiguration.TOPIC;

@Component
public class MqttService {

    @Resource
    private MqttClient mqttClient;

    public void sendMessage(String message) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttClient.publish(TOPIC, mqttMessage);
    }
}