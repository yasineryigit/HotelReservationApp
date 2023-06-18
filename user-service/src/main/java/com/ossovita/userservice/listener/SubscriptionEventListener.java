package com.ossovita.userservice.listener;

import com.ossovita.commonservice.enums.PaymentStatus;
import com.ossovita.kafka.model.SubscriptionPaymentResponse;
import com.ossovita.userservice.entity.Subscription;
import com.ossovita.userservice.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SubscriptionEventListener {

    SubscriptionService subscriptionService;

    public SubscriptionEventListener(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @KafkaListener(
            topics = "subscription-payment-response-topic",
            groupId = "foo",
            containerFactory = "subscriptionPaymentResponseKafkaListenerContainerFactory"//we need to assign containerFactory
    )
    public void listenSubscriptionPaymentResponse(SubscriptionPaymentResponse subscriptionPaymentResponse) {
        log.info("Subscription Payment Updated | ReservationPaymentResponseModel: " + subscriptionPaymentResponse.toString());
        Subscription subscriptionInDB = subscriptionService.getSubscription(subscriptionPaymentResponse.getSubscriptionFk());
        if(subscriptionPaymentResponse.getSubscriptionPaymentStatus().equals(PaymentStatus.PAID)){
            subscriptionInDB.setPaid(true);
            subscriptionService.save(subscriptionInDB);
        }
    }

}
