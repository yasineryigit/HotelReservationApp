package com.ossovita.accountingservice.business.abstracts;

import com.ossovita.accountingservice.core.entities.ReservationPayment;
import com.ossovita.accountingservice.core.entities.dto.request.ReservationPaymentRequest;

public interface ReservationPaymentService {
    ReservationPayment createReservationPayment(ReservationPaymentRequest reservationPaymentRequest) throws Exception;
}
