package com.lmk.mqtt.client;


import com.lmk.mqtt.service.TestRabbitMqService;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author lmk
 * @version 1.0.0
 * @ClassName MessageCallBack.java
 * @Description TODO
 * @createTime 2022-06-10 15:50:59
 */
public class MessageCallBack implements MqttCallback {

    private final Logger logger = LoggerFactory.getLogger(MessageCallBack.class);

    public MessageCallBack() {

    }

    @Override
    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
        logger.info("正在尝试重连");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // subscribe后得到的消息会执行到这里面
        logger.info("接收消息主题{}", topic);
        logger.info("接收消息Qos{}", message.getQos() + "");
        String record = new String(message.getPayload(), StandardCharsets.UTF_8);
        logger.info("接收消息内容{}", record);

        TestRabbitMqService.test(message.getPayload());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        logger.info("发送消息{}", token.isComplete() + "");
        logger.info("发送消息主题{}", Arrays.toString(token.getTopics()));
        try {
            logger.info("发布消息内容{}", token.getMessage() + "");
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
}
