package com.lmk.mqtt.client;

import com.lmk.mqtt.constrant.MqttConstant;

/**
 * @author lmk
 * @version 1.0.0
 * @ClassName MqClient.java
 * @Description TODO
 * @createTime 2022-06-27 19:42:24
 */
public class MqClient {
    public void init() {
        ClientMqtt client = ClientMqtt.createClient();
        client.defaultConfigSetting()
                .setAutoMaticReconnect(true)
                .setCleanSession(true)
                .setCallback(new MqMessageCallBack(client))
                .connect();
        client.subscribe(MqttConstant.SUBSCRIBE_TOPIC, 2);
    }
}
