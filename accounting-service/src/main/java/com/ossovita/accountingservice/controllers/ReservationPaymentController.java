package com.ossovita.accountingservice.controllers;

import com.ossovita.accountingservice.business.abstracts.ReservationPaymentService;
import com.ossovita.accountingservice.core.entities.ReservationPayment;
import com.ossovita.accountingservice.core.entities.dto.request.ReservationPaymentRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/1.0/accounting/reservation-payment")
public class ReservationPaymentController {

    ReservationPaymentService reservationPaymentService;

    public ReservationPaymentController(ReservationPaymentService reservationPaymentService) {
        this.reservationPaymentService = reservationPaymentService;
    }

    @PostMapping("/create-reservation-payment")
    public ReservationPayment createReservationPayment(@RequestBody ReservationPaymentRequest reservationPaymentRequest) throws Exception {
        return reservationPaymentService.createReservationPayment(reservationPaymentRequest);
    }


}
