package com.lmk.mqtt.service.api;

import com.lmk.mqtt.rabbit.exchange.ExchangeEnum;

public interface ExchangeService {
    void createDirectExchange(ExchangeEnum exchangeEnum);
    void createFanoutExchange(ExchangeEnum exchangeEnum);
    void createTopicExchange(ExchangeEnum exchangeEnum);
}
