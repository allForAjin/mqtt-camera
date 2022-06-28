package com.lmk.mqtt.service.api;

import com.lmk.mqtt.exchange.ExchangeEnum;
import com.lmk.mqtt.queue.QueueEnum;

public interface MqService {
    void send(Object msg);
    void send(Object msg,String routingKey);

    void send(Object msg,QueueEnum queueEnum,String routingKey);
    void send(Object msg,ExchangeEnum exchangeEnum,String routingKey);
    void send(Object msg, ExchangeEnum exchangeEnum, QueueEnum queueEnum,String routingKey);


}
