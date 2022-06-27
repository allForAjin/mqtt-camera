package com.lmk.mqtt;

import com.lmk.mqtt.service.TestClientService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MqttClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(RabbitMqClientApplication.class,args);
        new TestClientService().testClientInit();
    }
}
