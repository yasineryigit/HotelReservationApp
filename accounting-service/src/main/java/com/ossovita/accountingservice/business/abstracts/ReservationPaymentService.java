package com.ossovita.accountingservice.business.abstracts;

import com.ossovita.accountingservice.core.entities.ReservationPayment;
import com.ossovita.commonservice.core.payload.request.ReservationCreditCardPaymentRequest;

public interface ReservationPaymentService {

    String updateReservationPayment(ReservationCreditCardPaymentRequest reservationCreditCardPaymentRequest) throws Exception;
}
