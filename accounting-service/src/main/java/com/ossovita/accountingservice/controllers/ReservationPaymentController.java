package com.ossovita.accountingservice.controllers;

import com.ossovita.accountingservice.business.abstracts.ReservationPaymentService;
import com.ossovita.accountingservice.core.entities.ReservationPayment;
import com.ossovita.commonservice.core.entities.dtos.request.ReservationPaymentRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/1.0/accounting/reservation-payment")
public class ReservationPaymentController {

    ReservationPaymentService reservationPaymentService;

    public ReservationPaymentController(ReservationPaymentService reservationPaymentService) {
        this.reservationPaymentService = reservationPaymentService;
    }


    @PostMapping("/create-reservation-payment")
    public ReservationPayment createReservationPayment(@Valid @RequestBody ReservationPaymentRequest reservationPaymentRequest) throws Exception {
        return reservationPaymentService.createReservationPayment(reservationPaymentRequest);
    }


    @PutMapping("/update-reservation-payment")
    public String updateReservationPayment(@Valid @RequestBody ReservationPaymentRequest reservationPaymentRequest) throws Exception {
        return reservationPaymentService.updateReservationPayment(reservationPaymentRequest);
    }


}
