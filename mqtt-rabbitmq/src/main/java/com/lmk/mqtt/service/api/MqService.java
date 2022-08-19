package com.lmk.mqtt.service.api;

import com.lmk.mqtt.rabbit.exchange.ExchangeEnum;
import com.lmk.mqtt.rabbit.queue.DelayQueueEnum;
import com.lmk.mqtt.rabbit.queue.QueueEnum;

public interface MqService {
    /**
     * 默认发送方式
     * exchange和queue都为默认
     *
     * @param msg 消息
     */
    void send(Object msg);

    /**
     * 指定routingKey，队列交换机都默认
     *
     * @param msg        消息
     * @param routingKey routingKey
     */
    void send(Object msg, String routingKey);

    /**
     * 指定routingKey和队列，交换机默认
     *
     * @param msg        消息
     * @param queueEnum  队列名
     * @param routingKey routingKey
     */
    void send(Object msg, QueueEnum queueEnum, String routingKey);

    /**
     * 指定交换机与routingKey，无队列
     *
     * @param msg          消息
     * @param exchangeEnum 交换机
     * @param routingKey   routingKey
     */
    void send(Object msg, ExchangeEnum exchangeEnum, String routingKey);

    /**
     * 指定交换机与routingKey，无队列
     * 以 json 格式发送
     * @param msg          消息
     * @param exchangeEnum 交换机
     * @param routingKey   routingKey
     */
    void sendJson(Object msg, ExchangeEnum exchangeEnum, String routingKey);

    /**
     * 都指定
     *
     * @param msg          消息
     * @param exchangeEnum 交换机
     * @param queueEnum    队列
     * @param routingKey   routingKey
     */
    void send(Object msg, ExchangeEnum exchangeEnum, QueueEnum queueEnum, String routingKey);

    /**
     * 都指定
     * 发送json
     * @param msg          消息
     * @param exchangeEnum 交换机
     * @param queueEnum    队列
     * @param routingKey   routingKey
     */
    void sendJson(Object msg, ExchangeEnum exchangeEnum, QueueEnum queueEnum, String routingKey);

    void sendDelayMsg(Object msg, ExchangeEnum exchangeEnum, DelayQueueEnum delayQueueEnum, String routingKey,String deadRoutingKey);


}
