package com.ossovita.reservationservice.controllers;

import com.ossovita.reservationservice.business.abstracts.ReservationService;
import com.ossovita.reservationservice.core.entities.Reservation;
import com.ossovita.reservationservice.core.entities.dto.request.ReservationRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/1.0/reservation")
public class ReservationController {

    ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/status")
    public String getStatus() {
        return "Working..";
    }

    @PostMapping("/create-reservation")
    public Reservation createReservation(@Valid @RequestBody ReservationRequest reservationRequest) throws Exception {
        return reservationService.createReservation(reservationRequest);
    }

    @GetMapping("/is-reservation-available")
    public boolean isReservationAvailable(@RequestParam long reservationFk){
        return reservationService.isReservationAvailable(reservationFk);
    }

}
