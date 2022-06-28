package com.lmk.mqtt.service;

import com.lmk.mqtt.client.ClientMqtt;
import com.lmk.mqtt.client.MessageCallBack;
import com.lmk.mqtt.constrant.MqttConstant;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class TestClientService {
    public void testClientInit() {
        ClientMqtt client = ClientMqtt.createClient()
                .defaultConfigSetting()
                .setAutoMaticReconnect(true)
                .setCleanSession(true)
                .connect();
        client.subscribe(MqttConstant.SUBSCRIBE_TOPIC, 2);
        String msg = "{" +
                "\t\"cam_0\": {\n" +
                "\t\t\"coordinate_and_state\": [\n" +
                "\t\t\t[9, 78, 626, 486, 0],\n" +
                "\t\t\t[52, 80, 633, 484, 1],\n" +
                "\t\t\t[4, 79, 570, 485, 0]\n" +
                "\t\t],\n" +
                "\t\t\"saved_video\": \"0_2022-04-14_11:51:01.avi\"\n" +
                "\t},\n" +
                "\"cam_1\": {\n" +
                "\t\t\"coordinate_and_state\": [\n" +
                "\t\t\t[9, 78, 626, 486, 0],\n" +
                "\t\t\t[52, 80, 633, 484, 0],\n" +
                "\t\t\t[4, 79, 570, 485, 0]\n" +
                "\t\t],\n" +
                "\t\t\"saved_video\": null\n" +
                "\t}\n" +
                "}";
        new Thread(() -> {
            while (true) {
                client.publish(MqttConstant.SUBSCRIBE_TOPIC, msg.getBytes(), 2, false);
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "client " + client.getClient().getClientId() + " publish message").start();

    }


}
