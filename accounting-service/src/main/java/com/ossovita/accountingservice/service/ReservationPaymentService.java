package com.ossovita.accountingservice.service;

import com.ossovita.commonservice.core.payload.request.ReservationCreditCardPaymentRequest;

public interface ReservationPaymentService {

    String updateReservationPayment(ReservationCreditCardPaymentRequest reservationCreditCardPaymentRequest) throws Exception;
}
