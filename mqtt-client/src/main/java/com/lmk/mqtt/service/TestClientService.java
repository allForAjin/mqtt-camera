package com.lmk.mqtt.service;

import com.lmk.mqtt.client.ClientMqtt;
import com.lmk.mqtt.client.MessageCallBack;
import com.lmk.mqtt.client.MqClient;
import com.lmk.mqtt.client.MqMessageCallBack;
import com.lmk.mqtt.constrant.MqttConstant;

public class TestClientService {

    public void testClientInit() {
        ClientMqtt client = ClientMqtt.createClient();
        client.defaultConfigSetting()
                .setAutoMaticReconnect(true)
                .setCleanSession(true)
                .setCallback(new MessageCallBack(client))
                .connect();
        client.subscribe(MqttConstant.SUBSCRIBE_TOPIC, 2);
        client.publish(MqttConstant.SUBSCRIBE_TOPIC, "hello".getBytes(), 2, false);
    }


}
