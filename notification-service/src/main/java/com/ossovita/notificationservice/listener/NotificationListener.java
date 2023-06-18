package com.ossovita.notificationservice.listener;

import com.ossovita.kafka.model.NotificationRequest;
import com.ossovita.notificationservice.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationListener {

    private final EmailService emailService;

    public NotificationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(
            topics = "notification-request-topic",
            groupId = "foo",
            containerFactory = "notificationRequestConcurrentKafkaListenerContainerFactory"//we need to assign containerFactory
    )
    public void listenNotificationRequest(NotificationRequest notificationRequest) {
        log.info("listenNotificationRequest {}:  ", notificationRequest.toString());
        emailService.directEmail(notificationRequest.getTo(), notificationRequest.getNotificationType(), notificationRequest.getPayload());
        //TODO: add firebase mobile push notification
    }
}
