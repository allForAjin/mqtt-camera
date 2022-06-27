package com.lmk.mqtt;

import com.lmk.mqtt.entity.MqttProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lmk
 */
@SpringBootApplication
public class MqttServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MqttServerApplication.class, args);
    }

}
