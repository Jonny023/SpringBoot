package com.example.springbootmqtt.config;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttClientConfiguration {

    public static final String TOPIC = "test/topic";

    private String host;
    private String clientId;
    private String username;
    private String password;

    public void setHost(String host) {
        this.host = host;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Bean
    public MqttClient mqttClient() throws MqttException {
        // MqttClient mqttClient = new MqttClient(host, MqttClient.generateClientId());
        MqttClient mqttClient = new MqttClient(host, clientId);
        // mqttClient.setCallback(new MyMqttCallback());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        mqttClient.connect(options);
        mqttClient.subscribe(TOPIC);
        return mqttClient;
    }

}
