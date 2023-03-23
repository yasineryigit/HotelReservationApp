package com.ossovita.hotelservice.controllers;

import com.ossovita.commonservice.core.entities.Hotel;
import com.ossovita.commonservice.core.entities.dtos.HotelSaveFormDto;
import com.ossovita.hotelservice.business.abstracts.HotelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/1.0/hotel")
@Slf4j
public class HotelController {

    HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/status")
    public String getStatus() {
        log.info("api/1.0/hotel/status successfully reached");
        return "Working..";
    }

    @PostMapping("/create-hotel")
    public Hotel createHotel(@RequestBody HotelSaveFormDto hotelSaveFormDto) {
        return hotelService.createHotel(hotelSaveFormDto);
    }

    @GetMapping("/get-all-hotels")
    public List<Hotel> getAllHotels() {
        return hotelService.getAllHotels();
    }


}
