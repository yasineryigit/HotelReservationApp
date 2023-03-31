package com.ossovita.accountingservice.business.abstracts;

import com.ossovita.accountingservice.core.entities.ReservationPayment;
import com.ossovita.commonservice.core.entities.dtos.request.ReservationPaymentRequest;

public interface ReservationPaymentService {
    ReservationPayment createReservationPayment(ReservationPaymentRequest reservationPaymentRequest) throws Exception;

    String updateReservationPayment(ReservationPaymentRequest reservationPaymentRequest) throws Exception;
}
