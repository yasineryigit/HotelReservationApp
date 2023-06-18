package com.ossovita.userservice.producer;

import com.ossovita.commonservice.enums.NotificationType;
import com.ossovita.kafka.model.NotificationRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.ScriptAssert;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
public class CustomerEventProducer {

    KafkaTemplate<String, Object> kafkaTemplate;

    public CustomerEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCustomerWelcomeNotification(String userEmail, String userFirstName, String userLastName) {
        HashMap<String, String> payload = new HashMap<>();
        payload.put("customer_email", userEmail);
        payload.put("customer_first_name", userFirstName);
        payload.put("customer_last_name", userLastName);

        NotificationRequest notificationRequest =
                new NotificationRequest(userEmail, NotificationType.CUSTOMER_WELCOME_NOTIFICATION, payload);
        kafkaTemplate.send("notification-request-topic", notificationRequest);
        log.info("Customer welcome notification has been sent {}: " + notificationRequest.toString());
    }
}
