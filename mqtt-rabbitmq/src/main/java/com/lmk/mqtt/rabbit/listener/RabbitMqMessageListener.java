package com.lmk.mqtt.rabbit.listener;

import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

//@Component
public class RabbitMqMessageListener implements MessageListener {
    private final Logger logger = LoggerFactory.getLogger(RabbitMqMessageListener.class);
    @Override
    public void onMessage(Message message) {
        JSONObject jsonObject = JSONObject.parseObject(message.getBody());
        logger.info("接收到rabbitmq消息:{}",jsonObject.toJSONString());
    }
}
