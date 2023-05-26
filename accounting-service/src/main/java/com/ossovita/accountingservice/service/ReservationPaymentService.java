package com.ossovita.accountingservice.service;

import com.ossovita.commonservice.payload.request.ReservationCreditCardPaymentRequest;

public interface ReservationPaymentService {

    String createReservationPayment(ReservationCreditCardPaymentRequest reservationCreditCardPaymentRequest) throws Exception;
}
