package com.lmk.mqtt.service.api;

import com.lmk.mqtt.rabbit.queue.QueueEnum;

public interface QueueService {
    void createQueue(QueueEnum queueEnum);
}
