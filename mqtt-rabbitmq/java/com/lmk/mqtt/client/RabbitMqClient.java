package com.lmk.mqtt.client;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RabbitMqClient {
    @PostConstruct
    public void init(){
        ClientMqtt.createClient()
                .defaultConfigSetting()
                .setAutoMaticReconnect(true)
                .setCleanSession(true)
                .setCallback(new MqClientMessageCallBack())
                .connect();
    }
}
