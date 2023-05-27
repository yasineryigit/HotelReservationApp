package com.ossovita.accountingservice.service.impl;

import com.ossovita.accountingservice.entity.SubscriptionPayment;
import com.ossovita.accountingservice.payload.request.SubscriptionCreditCardPaymentRequest;
import com.ossovita.accountingservice.repository.SubscriptionPaymentRepository;
import com.ossovita.accountingservice.service.SubscriptionPaymentService;
import com.ossovita.clients.user.UserClient;
import com.ossovita.commonservice.dto.SubscriptionDto;
import com.ossovita.commonservice.enums.PaymentStatus;
import com.ossovita.commonservice.exception.IdNotFoundException;
import com.ossovita.kafka.model.SubscriptionPaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SubscriptionPaymentServiceImpl implements SubscriptionPaymentService {

    SubscriptionPaymentRepository subscriptionPaymentRepository;
    UserClient userClient;
    KafkaTemplate<String, Object> kafkaTemplate;

    public SubscriptionPaymentServiceImpl(SubscriptionPaymentRepository subscriptionPaymentRepository, UserClient userClient, KafkaTemplate<String, Object> kafkaTemplate) {
        this.subscriptionPaymentRepository = subscriptionPaymentRepository;
        this.userClient = userClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public SubscriptionPaymentResponse createSubscriptionPayment(SubscriptionCreditCardPaymentRequest subscriptionCreditCardPaymentRequest) {
        //check subscriptionFk -> price required | getSubscriptionDtoBySubscriptionFk
        //check bossFk -> isBossAvailable
        //check subscriptionDto.bossFk equals subscriptionCreditCardPaymentRequest.getBossFk() to prevent payment conflicts
        SubscriptionDto subscriptionDto = userClient.getSubscriptionDtoBySubscriptionFk(subscriptionCreditCardPaymentRequest.getSubscriptionFk());
        boolean isBossAvailable = userClient.isBossAvailable(subscriptionCreditCardPaymentRequest.getBossFk());
        if (subscriptionDto != null && isBossAvailable && subscriptionDto.getBossFk() == subscriptionCreditCardPaymentRequest.getBossFk()) {
            //TODO | implement payment provider
            //TODO | update reservationPayment object depends on payment status from the payment sdk (handle PAID & FAILED status)
            SubscriptionPayment subscriptionPayment = SubscriptionPayment.builder()
                    .subscriptionFk(subscriptionCreditCardPaymentRequest.getSubscriptionFk())
                    .bossFk(subscriptionCreditCardPaymentRequest.getBossFk())
                    .subscriptionPaymentAmount(subscriptionDto.getSubscriptionPrice())
                    .subscriptionPaymentStatus(PaymentStatus.PAID)
                    .build();
            SubscriptionPayment savedSubscriptionPayment = subscriptionPaymentRepository.save(subscriptionPayment);
            //return SubscriptionPaymentResponse
            SubscriptionPaymentResponse subscriptionPaymentResponse = SubscriptionPaymentResponse
                    .builder()
                    .subscriptionFk(savedSubscriptionPayment.getSubscriptionFk())
                    .subscriptionPaymentPk(savedSubscriptionPayment.getSubscriptionPaymentPk())
                    .bossFk(savedSubscriptionPayment.getBossFk())
                    .subscriptionPrice(savedSubscriptionPayment.getSubscriptionPaymentAmount())
                    .subscriptionPaymentStatus(savedSubscriptionPayment.getSubscriptionPaymentStatus())
                    .build();

            //publish kafka event to approve is paid status in user-service
            kafkaTemplate.send("subscription-payment-response-topic", subscriptionPaymentResponse);
            log.info("Payment Update Response sent | ReservationPaymentResponse: " + subscriptionPaymentResponse.toString());

            //return SubscriptionPaymentResponse
            return subscriptionPaymentResponse;//TODO | Hide pk

        } else {
            throw new IdNotFoundException("This request contains invalid ids");
        }


    }
}
