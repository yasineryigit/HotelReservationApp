package com.ossovita.notificationservice.consumer;

import com.ossovita.commonservice.payload.request.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationConsumer {

    @KafkaListener(
            topics = "notification-service-topic",
            groupId = "foo",
            containerFactory = "notificationRequestConcurrentKafkaListenerContainerFactory"//we need to assign containerFactory
    )
    public void consumeNotificationRequest(NotificationRequest notificationRequest) {
        log.info("Consumed {} from queue ", notificationRequest.toString());

    }
}
