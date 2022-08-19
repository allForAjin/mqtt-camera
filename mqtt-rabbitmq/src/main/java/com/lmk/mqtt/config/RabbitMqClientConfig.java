package com.lmk.mqtt.config;

import com.lmk.mqtt.client.ClientMqtt;
import com.lmk.mqtt.client.MqClientMessageCallBack;
import com.lmk.mqtt.client.PublishClientMessageCallBack;
import com.lmk.mqtt.constrant.MqttConstant;
import com.lmk.mqtt.entity.MqttConfig;
import com.lmk.mqtt.properties.MqttClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@EnableConfigurationProperties(MqttClientProperties.class)
public class RabbitMqClientConfig {
    @Autowired
    private MqClientMessageCallBack mqClientMessageCallBack;

    @Autowired
    private PublishClientMessageCallBack publishClientMessageCallBack;

    @Autowired
    private MqttClientProperties clientProperties;

    /**
     * mqtt client 配置 bean 注入
     */
    @Bean(name = "mqttConfig")
    public MqttConfig mqttConfig() {
        return new MqttConfig(
                clientProperties.getClientId(),
                clientProperties.getHost(),
                clientProperties.getUsername(),
                clientProperties.getPassword(),
                clientProperties.getWillTopic(),
                clientProperties.getWillQos()
        );
    }


    /**
     * mqtt client 初始化
     */
    @Bean(name = "rabbitMqttClient")
    @DependsOn(value = {"mqttConfig", "mqClientMessageCallBack"})
    public ClientMqtt mqttClient() {
        ClientMqtt clientMqtt = ClientMqtt.createClient();
        clientMqtt.setAutoMaticReconnect(false)
                .setCleanSession(true)
                .configSetting(mqttConfig())
                .setCallback(mqClientMessageCallBack)
                .connect();
        clientMqtt.subscribe(MqttConstant.SUBSCRIBE_TOPIC, MqttConstant.SUBSCRIBE_QOS);
        return clientMqtt;
    }



}
