package com.ossovita.hotelservice.controller;

import com.ossovita.commonservice.payload.request.HotelEmployeeRequest;
import com.ossovita.commonservice.payload.request.HotelRequest;
import com.ossovita.hotelservice.service.HotelService;
import com.ossovita.hotelservice.entity.Hotel;
import com.ossovita.hotelservice.payload.request.HotelWithImagesRequest;
import com.ossovita.hotelservice.payload.response.HotelEmployeeResponse;
import com.ossovita.hotelservice.utils.file.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/1.0/hotel")
@Slf4j
public class HotelController {

    @Value("${file.hotel-images.upload-dir}")
    private String hotelImagesUploadDir;

    HotelService hotelService;
    FileService fileService;

    public HotelController(HotelService hotelService, FileService fileService) {
        this.hotelService = hotelService;
        this.fileService = fileService;
    }

    @GetMapping("/status")
    public String getStatus() {
        log.info("api/1.0/hotel/status successfully reached");
        return "Working..";
    }

    @PostMapping("/create-hotel")
    public Hotel createHotel(@Valid @RequestBody HotelRequest hotelRequest) {
        return hotelService.createHotel(hotelRequest);
    }

    @PostMapping(value = "/create-hotel-with-hotel-images", consumes = "multipart/form-data")
    public Hotel createHotelWithHotelImages(@ModelAttribute HotelWithImagesRequest hotelWithImagesRequest) throws IOException {
        return hotelService.createHotelWithHotelImages(hotelWithImagesRequest);
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
    public ResponseEntity<HotelEmployeeResponse> createHotelEmployee(@Valid @RequestBody HotelEmployeeRequest hotelEmployeeRequest) {
        return ResponseEntity.ok(hotelService.createHotelEmployee(hotelEmployeeRequest));
    }


    @GetMapping("/uploads/hotel-images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveHotelImage(@PathVariable String filename) throws IOException {
        return fileService.serveImage(filename, hotelImagesUploadDir);
    }

}
