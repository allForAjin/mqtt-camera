package com.lmk.mqtt;

import com.lmk.mqtt.client.MqClient;

/**
 * @author lmk
 * @version 1.0.0
 * @ClassName RabbitMqClientApplication.java
 * @Description TODO
 * @createTime 2022-06-27 19:41:51
 */
public class RabbitMqClientApplication {
    public static void main(String[] args) {
        new MqClient().init();
    }
}
