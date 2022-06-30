package com.lmk.mqtt.client;

import com.lmk.mqtt.constrant.MqttConstant;
import com.lmk.mqtt.exchange.ExchangeEnum;
import com.lmk.mqtt.queue.DelayQueueEnum;
import com.lmk.mqtt.service.api.MqService;
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
public class MqClientMessageCallBack implements MqttCallback {

    private final Logger logger = LoggerFactory.getLogger(MqClientMessageCallBack.class);

    private ClientMqtt client;

    @Autowired
    private MqService mqService;


    @Override
    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
        logger.error("连接断开");
        client.reconnect();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // subscribe后得到的消息会执行到这里面
        logger.info("接收消息主题{}", topic);
        logger.info("接收消息Qos{}", message.getQos() + "");
        if (message.getPayload() != null) {
            mqService.send(message.getPayload(), ExchangeEnum.MQ_TOPIC_EXCHANGE, MqttConstant.ROUTING_KEY);
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        logger.info("发送消息{}", token.isComplete() + "");
        logger.info("发送消息主题{}", Arrays.toString(token.getTopics()));
        try {
            logger.info("发布消息内容{}", new String(token.getMessage().getPayload()) + "");
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

