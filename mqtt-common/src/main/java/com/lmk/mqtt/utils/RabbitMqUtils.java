package com.lmk.mqtt.utils;

import com.lmk.mqtt.exception.NullChannelException;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * @author lmk
 * @version 1.0.0
 * @ClassName RabbitMqUtils.java
 * @Description TODO
 * @createTime 2022-06-24 12:59:52
 */
public class RabbitMqUtils {
//    private static final String HOST = "10.200.44.212";
//    private static final String USERNAME = "lmk";
//    private static final String PASSWORD = "lmk";

    private static final String HOST = "192.168.10.132";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private static volatile ConnectionFactory factory;

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqUtils.class);

    private static final ConcurrentSkipListMap<Long, Object> publishMap = new ConcurrentSkipListMap<>();

    static {
        createFactory();
    }

    private static void createFactory() {
        if (factory == null) {
            synchronized (RabbitMqUtils.class) {
                if (factory == null) {
                    factory = new ConnectionFactory();
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
            return connection.createChannel();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void exchangeDeclareAndBind(Channel channel, String exchangeName, BuiltinExchangeType type, String routingKey, String queueName) {
        if (channel == null) {
            throw new NullChannelException("channel can not be null");
        }

        try {
            channel.exchangeDeclare(exchangeName, type);
            channel.queueBind(queueName, exchangeName, routingKey);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void queueDeclare(Channel channel, String queueName, boolean durable,
                                    boolean exclusive, boolean autoDelete, Map<String, Object> arguments) {
        if (channel == null) {
            throw new NullChannelException("channel can not be null");
        }
        try {
            channel.queueDeclare(queueName, durable, exclusive, autoDelete, arguments);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void publish(Channel channel, String exchangeName, String routingKey, byte[] message) {
        if (channel == null) {
            throw new NullChannelException("channel can not be null");
        }
        try {
            ConfirmCallback ackConfirmCallBack = (deliveryTag, multiple) -> {
                publishMap.headMap(deliveryTag).clear();
                logger.info("rabbitmq确认回调{}", message);
            };

            ConfirmCallback nackConfirmCallBack = (deliveryTag, multiple) -> {
                logger.debug("rabbitmq未确认回调{}", publishMap.get(deliveryTag));
            };
            channel.addConfirmListener(ackConfirmCallBack, nackConfirmCallBack);
            channel.basicPublish(exchangeName, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, message);
            publishMap.put(channel.getNextPublishSeqNo(), message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void consume(Channel channel, String queueName, DeliverCallback deliverCallback, CancelCallback cancelCallback) {
        try {
            channel.basicConsume(queueName, false, deliverCallback, cancelCallback);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
