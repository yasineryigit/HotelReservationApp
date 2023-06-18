package com.ossovita.accountingservice.listener;

import com.ossovita.accountingservice.entity.ReservationPayment;
import com.ossovita.accountingservice.service.ReservationPaymentService;
import com.ossovita.kafka.model.ReservationPaymentRefundRequest;
import com.ossovita.stripe.service.StripePaymentService;
import com.stripe.exception.StripeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ReservationPaymentListener {

    ReservationPaymentService reservationPaymentService;
    StripePaymentService stripePaymentService;

    public ReservationPaymentListener(ReservationPaymentService reservationPaymentService, StripePaymentService stripePaymentService) {
        this.reservationPaymentService = reservationPaymentService;
        this.stripePaymentService = stripePaymentService;
    }

    @KafkaListener(
            topics = "reservation-payment-refund-request-topic",
            groupId = "foo",
            containerFactory = "reservationPaymentRefundRequestKafkaListenerContainerFactory"//we need to assign containerFactory
    )
    public void consumeReservationPaymentRefundRequest(ReservationPaymentRefundRequest reservationPaymentRefundRequest) {
        //retrieve reservationPayment object from the database
        ReservationPayment reservationPaymentInDB = reservationPaymentService.getReservationPayment(reservationPaymentRefundRequest.getReservationPaymentPk());
        //prepare metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("reservation_payment_pk", reservationPaymentRefundRequest.getReservationPaymentPk());
        metadata.put("message", reservationPaymentRefundRequest.getReservationPaymentRefundMessage());
        //prepare params
        Map<String, Object> params = new HashMap<>();
        params.put("charge", reservationPaymentInDB.getReservationPaymentStripeChargeId());
        params.put("metadata", metadata);
        params.put("reason", reservationPaymentRefundRequest.getReservationPaymentRefundReason());

        try {
            //refund balance
            stripePaymentService.createPaymentRefund(reservationPaymentInDB.getReservationPaymentStripeChargeId(), params);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

    }

}
