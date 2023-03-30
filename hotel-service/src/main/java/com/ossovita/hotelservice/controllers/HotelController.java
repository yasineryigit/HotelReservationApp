package com.ossovita.hotelservice.controllers;

import com.ossovita.commonservice.core.entities.dtos.request.HotelEmployeeRequest;
import com.ossovita.commonservice.core.entities.dtos.request.HotelRequest;
import com.ossovita.commonservice.core.entities.dtos.response.HotelEmployeeResponse;
import com.ossovita.hotelservice.business.abstracts.HotelService;
import com.ossovita.hotelservice.core.entities.Hotel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public Hotel createHotel(@RequestBody HotelRequest hotelRequest) {
        return hotelService.createHotel(hotelRequest);
    }

    @GetMapping("/get-all-hotels")
    public List<Hotel> getAllHotels() {
        return hotelService.getAllHotels();
    }


    @GetMapping("/is-hotel-available")
    public boolean isHotelAvailable(@RequestParam long hotelPk) {
        return hotelService.isHotelAvailable(hotelPk);
    }


    @PostMapping("/create-hotel-employee")
    public ResponseEntity<HotelEmployeeResponse> createHotelEmployee(@RequestBody HotelEmployeeRequest hotelEmployeeRequest) {
        return ResponseEntity.ok(hotelService.createHotelEmployee(hotelEmployeeRequest));
    }


}
