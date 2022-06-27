package com.lmk.mqtt;


import com.lmk.mqtt.entity.MqttConfig;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class ClientMqtt {
    public static final MqttCallback defaultMqttCallback=new DefaultMqttCallBack();
    public static final MqttClientPersistence defaultPersistence = new MemoryPersistence();
    public static final MqttConfig defaultMqttConfig = new MqttConfig();
    private final Map<String,SubscribeTopic> subscribeTopicMap = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(ClientMqtt.class);

    private MqttClient client;
    private MqttConnectOptions options;
    private MqttConfig mqttConfig;
    private MqttClientPersistence persistence;
    private MqttCallback callback=defaultMqttCallback;
    private boolean cleanSession=MqttConnectOptions.CLEAN_SESSION_DEFAULT;
    private boolean automaticReconnect = true;
    private Integer keepAlive=MqttConnectOptions.KEEP_ALIVE_INTERVAL_DEFAULT;
    private Integer connectTimeOut = MqttConnectOptions.CONNECTION_TIMEOUT_DEFAULT;


    /**
     * 创建ClientMqtt客户端对象
     * 使用此类需要先使用该方法
     * @return this
     */
    public static ClientMqtt createClient() {
        return new ClientMqtt();
    }

    /**
     * 设置cleanSession是否清除会话
     * @param cleanSession 清除会话 true false（默认MqttConnectOptions.CLEAN_SESSION_DEFAULT=true）
     * @return this
     */
    public ClientMqtt setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
        return this;
    }

    /**
     * 设置是否自动重连
     * @param automaticReconnect 是否自动重连 true false（默认true）
     * @return this
     */
    public ClientMqtt setAutoMaticReconnect(boolean automaticReconnect){
        this.automaticReconnect = automaticReconnect;
        return this;
    }

    /**
     * 设置客户端超时时间
     * @param timeOut 超时时间（秒），默认 MqttConnectOptions.CONNECTION_TIMEOUT_DEFAULT=30
     * @return this
     */
    public ClientMqtt setConnectTimeOut(int timeOut){
        this.connectTimeOut = timeOut;
        return this;
    }

    /**
     * 设置心跳时间
     * @param keepAlive 心跳时间（秒），默认 MqttConnectOptions.KEEP_ALIVE_INTERVAL_DEFAULT=60
     * @return this
     */
    public ClientMqtt setKeepAlive(int keepAlive){
        this.keepAlive = keepAlive;
        return this;
    }

    /**
     * 默认设置
     * @return this
     */
    public ClientMqtt defaultConfigSetting() {
        if (mqttConfig==null){
            this.mqttConfig = defaultMqttConfig;
        }
        return this;
    }

    /**
     * mqtt配置类设置
     * @param mqttConfig 传入配置类
     * @return this
     */
    public ClientMqtt configSetting(MqttConfig mqttConfig) {
        this.mqttConfig = mqttConfig;
        return this;
    }

    /**
     * 设置mqtt回调
     * @param mqttCallback 回调
     * @return this
     */
    public ClientMqtt setCallback(MqttCallback mqttCallback) {
        this.callback = mqttCallback;
        return this;
    }

    /**
     * mqtt初始化连接
     * @return this
     */
    public ClientMqtt connect() {
        if (client == null) {
            if(persistence==null){
                persistence = defaultPersistence;
            }
            if (mqttConfig == null) {
                this.mqttConfig = defaultMqttConfig;
            }
            try {
                client = new MqttClient(mqttConfig.getHost(), mqttConfig.getClientId(), persistence);
            } catch (MqttException e) {
                e.printStackTrace();
                logger.error("connect()-----mqtt客户端创建连接失败");
                return this;
            }
            if (callback==null){
                callback = defaultMqttCallback;
            }
            client.setCallback(callback);
        }
        doConnect();
        return this;
    }

    /**
     * 执行mqtt连接
     */
    private void doConnect() {
        if (!client.isConnected()) {
            if (options == null) {
                options = this.createOptions();
            }
            try {
                client.connect(options);
            } catch (MqttException e) {
                e.printStackTrace();
                logger.error("doConnect()-----mqtt客户端连接失败");
            }
        }
    }


    /**
     * 创建 MqttConnectOptions 对象并设置值
     * @return MqttConnectOptions
     */
    public MqttConnectOptions createOptions() {
        options = new MqttConnectOptions();
        //判断mqtt配置是否初始化
        defaultConfigSetting();
        options.setCleanSession(cleanSession);
        options.setUserName(mqttConfig.getUsername());
        options.setPassword(mqttConfig.getPassword().toCharArray());
        options.setConnectionTimeout(connectTimeOut);
        options.setKeepAliveInterval(keepAlive);
        options.setAutomaticReconnect(automaticReconnect);
        return options;
    }


    /**
     * 客户端是否连接
     * client为空表示未连接
     * client.isConnected()为false表示未连接
     * @return true连接，false未连接
     */
    public boolean isConnected() {
        return client != null && client.isConnected();
    }

    /**
     * 断开客户端与服务端的连接
     */
    public void disconnect() {
        try {
            if (client != null && client.isConnected()) {
                client.disconnect();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新订阅主题
     */
    public void reSubscribe() {
        for (Map.Entry<String,SubscribeTopic> entry:subscribeTopicMap.entrySet()){
            subscribe(entry.getValue().getTopic(),entry.getValue().getQos());
        }
    }

    /**
     * 订阅主题（单个）
     * @param topic 主题
     * @param qos 服务质量等级QoS
     */
    public void subscribe(String topic, int qos) {
        try {
            client.subscribe(topic, qos);
            subscribeTopicMap.put(topic,new SubscribeTopic(topic,qos));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("subscribe-----订阅失败:"+topic);
        }
    }

    /**
     * 消息发布
     * @param topic 目标主题
     * @param message 消息
     * @param qos 服务质量等级QoS
     * @param retained 是否保留消息
     */
    public void publish(String topic, String message, int qos, boolean retained) {
        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(qos);
            mqttMessage.setRetained(retained);
            client.publish(topic, mqttMessage);
        } catch (Exception e) {
            logger.error("publish-----发布消息失败:"+topic+",消息内容："+message);
            e.printStackTrace();
        }
    }

    /**
     * 取消订阅
     * @param topic 取消订阅主题
     */
    public void unsubscribe(String topic){
        try {
            client.unsubscribe(topic);
            subscribeTopicMap.remove(topic);
        } catch (MqttException e) {
            logger.error("unsubscribe-----取消订阅失败:"+topic);
            e.printStackTrace();
        }
    }

    /**
     * 判断主题是否订阅
     * @param topic 主题
     * @return ture表示已订阅，false表示未订阅
     */
    public boolean isSubscribed(String topic){
        return subscribeTopicMap.containsKey(topic);
    }

    public MqttConfig getDefaultMqttConfig(){
        return defaultMqttConfig;
    }


    public MqttConfig getMqttConfig() {
        return mqttConfig;
    }


    public MqttClient getClient() {
        return client;
    }

    private static class DefaultMqttCallBack implements MqttCallback {

        @Override
        public void connectionLost(Throwable cause) {

        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            logger.info("接收消息主题 {}", topic);
            logger.info("接收消息Qos {}", message.getQos() + "");
            String record = new String(message.getPayload(), StandardCharsets.UTF_8);
            logger.info("接收消息内容 {}", record);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            logger.info("发送消息 {}", token.isComplete() + "");
            logger.info("发送消息主题 {}", Arrays.toString(token.getTopics()));
            try {
                logger.info("发布消息内容 {}", token.getMessage() + "");
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public static class SubscribeTopic{
        private final String topic;
        private final Integer qos;

        public SubscribeTopic(String topic, Integer qos) {
            this.topic = topic;
            this.qos = qos;
        }

        public String getTopic() {
            return topic;
        }

        public Integer getQos() {
            return qos;
        }

        @Override
        public String toString() {
            return "SubscribeTopic{" +
                    "topic='" + topic + '\'' +
                    ", qos=" + qos +
                    '}';
        }
    }
}
