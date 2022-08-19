package com.lmk.mqtt.entity;

import java.util.UUID;

public class MqttConfig {
    /**
     * 默认 ip 为 tcp 连接，地址为本机 ip，端口 1883
     */
    private static final String DEFAULT_HOST = "tcp://127.0.0.1:1883";
    /**
     * 默认用户认证配置
     */
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "public";
    /**
     * 默认 clientId 为 UUID
     */
    private static final String DEFAULT_CLIENT_ID=UUID.randomUUID().toString();
    /**
     * 默认遗嘱与 clientId 相同
     */
    private static final String DEFAULT_WILL_TOPIC=DEFAULT_CLIENT_ID;
    /**
     * 默认遗嘱QoS
     */
    private static final Integer WILL_QOS = 0;

    private String clientId;
    private String host;
    private String username;
    private String password;
    private String willTopic;
    private Integer willQos;



    public MqttConfig() {
        this(DEFAULT_CLIENT_ID, DEFAULT_HOST, DEFAULT_USERNAME, DEFAULT_PASSWORD, DEFAULT_WILL_TOPIC, WILL_QOS);
    }

    public MqttConfig(String clientId, String host, String username, String password) {
        this(clientId, host, username, password, DEFAULT_WILL_TOPIC, WILL_QOS);
        this.clientId = clientId;
        this.host = host;
        this.username = username;
        this.password = password;
    }

    public MqttConfig(String clientId, String host, String username, String password, String willTopic, Integer willQos) {
        this.clientId = clientId;
        this.host = host;
        this.username = username;
        this.password = password;
        this.willTopic = willTopic;
        this.willQos = willQos;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWillTopic() {
        return willTopic;
    }

    public void setWillTopic(String willTopic) {
        this.willTopic = willTopic;
    }

    public Integer getWillQos() {
        return willQos;
    }

    public void setWillQos(Integer willQos) {
        this.willQos = willQos;
    }

    @Override
    public String toString() {
        return "MqttConfig{" +
                "clientId='" + clientId + '\'' +
                ", host='" + host + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", willTopic='" + willTopic + '\'' +
                ", willQos=" + willQos +
                '}';
    }

    public boolean configIsLegal() {
        return clientId != null && !clientId.equals("")
                && host != null && !host.equals("")
                && username != null && !username.equals("")
                && password != null && !password.equals("")
                && willTopic != null && !willTopic.equals("")
                && willQos != null;
    }
}
