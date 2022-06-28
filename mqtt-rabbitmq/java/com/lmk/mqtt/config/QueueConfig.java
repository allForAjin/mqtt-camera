package com.lmk.mqtt.config;

import com.lmk.mqtt.queue.QueueEnum;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {
    @Bean
    public Queue createNormalQueue(){
        QueueEnum queueEnum = QueueEnum.DEFAULT_QUEUE;
        return new Queue(queueEnum.getQueueName(),queueEnum.isDurable(),true,false,queueEnum.getArguments());
    }
}
