package com.example.springbootmqtt.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * 回调处理
 */
@Slf4j
public class MyMqttCallback implements MqttCallback {

    @Override
    public void connectionLost(Throwable throwable) {
        log.error("回调异常：", throwable);
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        log.info("接收到消息：{}", new String(mqttMessage.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.info("deliveryComplete：{}", iMqttDeliveryToken);
    }
}