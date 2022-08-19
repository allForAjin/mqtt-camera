package com.lmk.mqtt.client;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class PublishClientMessageCallBack implements MqttCallback {
    private final Logger logger = LoggerFactory.getLogger(MqClientMessageCallBack.class);
    private ClientMqtt client;

    @Override
    public void connectionLost(Throwable throwable) {
        // 连接丢失后，在这里面进行重连
        logger.error("connection lost");
        client.reconnect();
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        logger.info("messageArrived,topic---{},qos---{},content---{}", topic, mqttMessage.getQos(), new String(mqttMessage.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        try {
            logger.info("deliveryComplete,isComplete--{},topic---{},content---{}", token.isComplete(), Arrays.toString(token.getTopics()), new String(token.getMessage().getPayload()));
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    @Lazy
    public void setClient(ClientMqtt client) {
        this.client = client;
    }
}
