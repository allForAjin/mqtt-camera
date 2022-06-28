package com.lmk.mqtt;

import com.lmk.mqtt.client.ClientMqtt;
import com.lmk.mqtt.client.MessageCallBack;
import com.lmk.mqtt.service.TestClientService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class MqttClientApplication {
    public static void main(String[] args) {
        new TestClientService().testClientInit();
    }
}
