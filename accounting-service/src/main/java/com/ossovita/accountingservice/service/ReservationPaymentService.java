package com.ossovita.accountingservice.service;

import com.ossovita.accountingservice.dto.CreatePaymentResponse;
import com.ossovita.accountingservice.payload.request.ReservationPaymentRequest;
import com.stripe.exception.StripeException;

public interface ReservationPaymentService {

    CreatePaymentResponse createReservationPaymentIntent(ReservationPaymentRequest reservationPaymentRequest);
}
