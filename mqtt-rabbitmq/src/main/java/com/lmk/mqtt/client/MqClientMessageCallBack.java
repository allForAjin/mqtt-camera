package com.lmk.mqtt.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lmk.mqtt.constrant.MqttConstant;
import com.lmk.mqtt.rabbit.exchange.ExchangeEnum;
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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Component
public class MqClientMessageCallBack implements MqttCallback {

    private final Logger logger = LoggerFactory.getLogger(MqClientMessageCallBack.class);

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private ClientMqtt client;

    @Autowired
    private MqService mqService;


    @Override
    public void connectionLost(Throwable cause) {
        // 连接丢失后，在这里面进行重连
        logger.error("connection lost");
        client.reconnect();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        if (message.getPayload() != null) {
            String data = new String(message.getPayload()).trim().replace("\n", "").replace("\t", "");
            // subscribe后得到的消息会执行到这里面
            logger.info("messageArrived,topic---{},qos---{},content---{}", topic, message.getQos(), data);
            try {
                JSONObject jsonObject = JSONObject.parseObject(data);
                // 加入localtime和tenantId两个字段
                jsonObject.put(MqttConstant.LOCAL_TIME_KEY, dateFormat.format(new Date()));
                jsonObject.put(MqttConstant.TENANT_ID_KEY, MqttConstant.TENANT_ID_VALUE);
                logger.info(jsonObject.toJSONString());
                // 消息转发至 rabbitmq
                mqService.sendJson(JSON.toJSONString(jsonObject), ExchangeEnum.MQ_TOPIC_EXCHANGE, MqttConstant.ROUTING_KEY);
            } catch (Exception e) {
                logger.error("message convert to json error,message---{},exception---{}",data,e.toString());
            }finally {
                data = null;
            }
        }

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

