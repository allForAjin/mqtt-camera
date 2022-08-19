package com.lmk.mqtt.constrant;

public class MqttConstant {
    public static final String SUBSCRIBE_TOPIC = "camera";

    public static final String PUBLISH_TOPIC = "publish";

    public static final Integer SUBSCRIBE_QOS = 2;

    public static final String MQ_QUEUE_NAME = "camera";
    public static final String EXCHANGE_NAME = "topic.exchange";
    public static final String ROUTING_KEY = "topic.routekey.app.accept.videoMsg";
    public static final String LOCAL_TIME_KEY = "localtime";
    public static final String TENANT_ID_KEY = "tenantId";
    public static final Integer TENANT_ID_VALUE = 1;


}
