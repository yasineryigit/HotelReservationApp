package com.ossovita.accountingservice.service;

import com.ossovita.commonservice.payload.response.CreatePaymentResponse;
import com.ossovita.accountingservice.payload.request.ReservationPaymentRequest;
import com.stripe.model.Charge;

public interface ReservationPaymentService {

    CreatePaymentResponse createReservationPaymentIntent(ReservationPaymentRequest reservationPaymentRequest);

    void processReservationPaymentCharge(Charge charge);
}
