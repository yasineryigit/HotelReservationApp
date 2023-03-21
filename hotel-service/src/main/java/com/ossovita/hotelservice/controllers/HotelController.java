package com.ossovita.hotelservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/1.0/hotel")
@Slf4j
public class HotelController {

    @GetMapping("/status")
    public String getStatus() {
        log.info("api/1.0/hotel/status successfully reached");
        return "Working..";
    }
}
