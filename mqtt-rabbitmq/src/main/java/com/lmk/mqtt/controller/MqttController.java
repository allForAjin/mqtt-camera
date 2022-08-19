package com.lmk.mqtt.controller;

import com.lmk.mqtt.constrant.MqttConstant;
import com.lmk.mqtt.entity.result.ResponseResult;
import com.lmk.mqtt.service.api.MqttGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mqtt")
public class MqttController {
    private Logger logger = LoggerFactory.getLogger(MqttController.class);
    @Autowired
    private MqttGateway mqttGateway;

    @PostMapping("/publish")
    public ResponseResult publish(@RequestParam("message") String message) {
        if (!StringUtils.hasText(message)) {
            return ResponseResult.fail("message Illegal");
        }
        ResponseResult result = null;
        try {
            mqttGateway.sendToMqtt(MqttConstant.PUBLISH_TOPIC, message);
            result = ResponseResult
                    .success()
                    .addData("topic", MqttConstant.PUBLISH_TOPIC)
                    .addData("message", message);
        } catch (Exception e) {
            result = ResponseResult.fail();
            logger.error("client publish message error,topic---{},message---{}", MqttConstant.SUBSCRIBE_TOPIC, message);
        }
        return result;
    }
}
