package com.ossovita.accountingservice.producer;

import com.ossovita.kafka.model.ReservationPaymentResponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReservationPaymentEventProducer {

    KafkaTemplate<String, Object> kafkaTemplate;

    public ReservationPaymentEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendReservationPaymentEvent(ReservationPaymentResponse reservationPaymentResponse) {
        //send kafka message to the reservation-service to update its reservationStatus & reservationIsApproved fields
        kafkaTemplate.send("reservation-payment-response-topic", reservationPaymentResponse);
    }
}
