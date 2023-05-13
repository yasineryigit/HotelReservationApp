package com.ossovita.accountingservice.service;

import com.ossovita.commonservice.payload.request.ReservationCreditCardPaymentRequest;

public interface ReservationPaymentService {

    String updateReservationPayment(ReservationCreditCardPaymentRequest reservationCreditCardPaymentRequest) throws Exception;
}
