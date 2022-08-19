package com.lmk.mqtt.config;

import com.lmk.mqtt.properties.MqttClientProperties;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
@EnableConfigurationProperties(MqttClientProperties.class)
public class PublishClientConfig {

    @Autowired
    private MqttClientProperties clientProperties;

    @Bean(name = "mqttConnectOptions")
    public MqttConnectOptions mqttConnectOptions(){
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{clientProperties.getHost()});
        options.setUserName(clientProperties.getUsername());
        options.setPassword(clientProperties.getPassword().toCharArray());
        options.setCleanSession(true);
        return options;
    }

    @Bean(name = "mqttPahoClientFactory")
    public MqttPahoClientFactory mqttPahoClientFactory(){
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectOptions());
        return factory;
    }

    @Bean(name = "mqttPublishHandler")
    @ServiceActivator(inputChannel = "mqttPublishChannel")
    public MessageHandler mqttPublishHandler(){
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler("publish-client",mqttPahoClientFactory()){
            @Override
            public void messageArrived(String topic, MqttMessage message) {
            }
        };
        handler.setAsync(true);
        return handler;
    }

    @Bean(name = "mqttPublishChannel")
    public MessageChannel mqttPublishChannel() {
        return new DirectChannel();
    }

}
