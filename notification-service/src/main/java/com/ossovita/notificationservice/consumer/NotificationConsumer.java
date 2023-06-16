package com.ossovita.notificationservice.consumer;

import com.ossovita.kafka.model.NotificationRequest;
import com.ossovita.notificationservice.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationConsumer {

    private final EmailService emailService;

    public NotificationConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(
            topics = "notification-request-topic",
            groupId = "foo",
            containerFactory = "notificationRequestConcurrentKafkaListenerContainerFactory"//we need to assign containerFactory
    )
    public void consumeNotificationRequest(NotificationRequest notificationRequest) {
        log.info("Consumed {} from queue ", notificationRequest.toString());
        emailService.send(notificationRequest.getTo(), notificationRequest.getNotificationType(), notificationRequest.getPayload());
    }
}
