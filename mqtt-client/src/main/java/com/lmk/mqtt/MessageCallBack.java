package com.lmk.mqtt;

import android.util.Log;

import com.lmk.mqtt.service.ServerMessageCallBack;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lmk
 * @version 1.0.0
 * @ClassName MessageCallBack.java
 * @Description TODO
 * @createTime 2022-06-10 15:50:59
 */
public class MessageCallBack implements MqttCallback {
    private static final String TAG=MessageCallBack.class.getSimpleName();
    private ServerMessageCallBack serverMessageCallBack;
    public MessageCallBack(){

    }

    public MessageCallBack(ServerMessageCallBack serverMessageCallBack){
        this.serverMessageCallBack = serverMessageCallBack;
    }
    @Override
    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
        Log.i(TAG,"正在尝试重连");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // subscribe后得到的消息会执行到这里面
        Log.i("接收消息主题" , topic);
        Log.i("接收消息Qos" , message.getQos()+"");
        String record=new String(message.getPayload(), StandardCharsets.UTF_8);
        Log.i("接收消息内容" ,record);

        Map<String,Object> map = new HashMap<>();
        map.put("topic",topic);
        map.put("qos",message.getQos());
        map.put("msg",record);

//        serverMessageCallBack.sendMessage(map.toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.i("发送消息", token.isComplete()+"");
        Log.i("发送消息主题", Arrays.toString(token.getTopics()));
        try {
            Log.i("发布消息内容：",token.getMessage()+"");
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
}
