package com.ossovita.reservationservice.controllers;

import com.ossovita.commonservice.core.entities.dtos.CreateReservationDto;
import com.ossovita.reservationservice.business.abstracts.ReservationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/1.0/reservation")
public class ReservationController {

    ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/status")
    public String getStatus(){
        return "Working..";
    }


    @PostMapping("/create-reservation")
    public CreateReservationDto createReservation(@RequestBody CreateReservationDto createReservationDto){
        return reservationService.createReservation(createReservationDto);
    }


}
