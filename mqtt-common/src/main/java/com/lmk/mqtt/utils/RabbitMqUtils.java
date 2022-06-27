package com.lmk.mqtt.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author lmk
 * @version 1.0.0
 * @ClassName RabbitMqUtils.java
 * @Description TODO
 * @createTime 2022-06-24 12:59:52
 */
public class RabbitMqUtils {
    private static final String HOST = "192.168.10.132";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private static volatile ConnectionFactory factory;

    static {
        createFactory();
    }

    private static void createFactory(){
        if (factory==null){
            synchronized (RabbitMqUtils.class){
                if (factory==null){
                    factory= new ConnectionFactory();
                    factory.setHost(HOST);
                    factory.setUsername(USERNAME);
                    factory.setPassword(PASSWORD);
                }
            }
        }
    }

    public static Channel getChannel() {
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            return channel;
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void publish(Channel channel,String exchangeName,String exchangeType,String routingKey,String queueName,String message){
        try {
            channel.exchangeDeclare(exchangeName,exchangeType);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
