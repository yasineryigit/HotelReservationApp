package com.ossovita.accountingservice.controller;

import com.ossovita.accountingservice.dto.CreatePaymentResponse;
import com.ossovita.accountingservice.service.ReservationPaymentService;
import com.ossovita.accountingservice.payload.request.ReservationPaymentRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/1.0/accounting/reservation-payment")
public class ReservationPaymentController {

    ReservationPaymentService reservationPaymentService;

    public ReservationPaymentController(ReservationPaymentService reservationPaymentService) {
        this.reservationPaymentService = reservationPaymentService;
    }


    @PostMapping("/create-reservation-payment-intent")
    public CreatePaymentResponse createReservationPayment(@Valid @RequestBody ReservationPaymentRequest reservationPaymentRequest)  {
        return reservationPaymentService.createReservationPaymentIntent(reservationPaymentRequest);
    }




}
