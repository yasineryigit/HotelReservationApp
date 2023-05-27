package com.ossovita.accountingservice.service;

import com.ossovita.accountingservice.payload.request.ReservationCreditCardPaymentRequest;

public interface ReservationPaymentService {

    String createReservationPayment(ReservationCreditCardPaymentRequest reservationCreditCardPaymentRequest) throws Exception;
}
