package com.ossovita.reservationservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/reservation")
public class ReservationController {

    @GetMapping("/status")
    public String getStatus(){
        return "Working..";
    }
}
