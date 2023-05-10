package com.ossovita.userservice.business.abstracts.feign;

import com.ossovita.commonservice.core.payload.request.HotelEmployeeRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "hotel-service", url = "http://localhost:8888/api/1.0/hotel")
public interface HotelClient {

    @PostMapping("/create-hotel-employee")
    ResponseEntity<Object> createHotelEmployee(@RequestBody HotelEmployeeRequest hotelEmployeeRequest);

    @GetMapping("/is-hotel-available")
    boolean isHotelAvailable(@RequestParam long hotelPk);

}
