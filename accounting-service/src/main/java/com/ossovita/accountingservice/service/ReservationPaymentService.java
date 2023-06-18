package com.ossovita.accountingservice.service;

import com.ossovita.accountingservice.entity.ReservationPayment;
import com.ossovita.accountingservice.payload.response.CreatePaymentResponse;
import com.ossovita.accountingservice.payload.request.ReservationPaymentRequest;
import com.stripe.model.Charge;

public interface ReservationPaymentService {

    CreatePaymentResponse createReservationPaymentIntent(ReservationPaymentRequest reservationPaymentRequest);

    void processReservationPaymentCharge(Charge charge);

    ReservationPayment getReservationPayment(long reservationPaymentPk);
}
