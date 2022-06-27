package com.lmk.mqtt.service;

import com.lmk.mqtt.client.ClientMqtt;
import com.lmk.mqtt.client.MessageCallBack;
import com.lmk.mqtt.constrant.MqttConstant;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Scanner;

public class TestClientService {

    public void testClientInit(){
        ClientMqtt client = ClientMqtt.createClient()
                .defaultConfigSetting()
                .setAutoMaticReconnect(true)
                .setCleanSession(true)
                .setCallback(new MessageCallBack())
                .connect();
        client.subscribe(MqttConstant.SUBSCRIBE_TOPIC,2);
    }


}
