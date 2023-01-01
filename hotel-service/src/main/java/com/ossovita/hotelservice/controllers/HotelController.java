package com.ossovita.hotelservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/hotel")
public class HotelController {

    @GetMapping("/status")
    public String getStatus() {
        return "Working..";
    }
}
