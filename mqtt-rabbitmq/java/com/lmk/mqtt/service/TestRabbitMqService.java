package com.lmk.mqtt.service;

import com.lmk.mqtt.constrant.MqttConstant;
import com.lmk.mqtt.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;


public class TestRabbitMqService {
    public static void test(byte[] message){
        Channel channel = RabbitMqUtils.getChannel();
        RabbitMqUtils.queueDeclare(channel, MqttConstant.MQ_QUEUE_NAME, true, false, false, null);
        RabbitMqUtils.exchangeDeclareAndBind(channel, MqttConstant.EXCHANGE_NAME, BuiltinExchangeType.DIRECT, MqttConstant.ROUTING_KEY, MqttConstant.MQ_QUEUE_NAME);
        RabbitMqUtils.publish(channel, MqttConstant.EXCHANGE_NAME, MqttConstant.ROUTING_KEY, message);
    }
}
