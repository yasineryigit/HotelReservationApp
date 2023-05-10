package com.ossovita.accountingservice.business.abstracts.feign;

import com.ossovita.commonservice.core.dto.ReservationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(value = "reservation-service", url = "http://localhost:8888/api/1.0/reservation")
public interface ReservationClient {

    @GetMapping("/get-reservation-dto-by-reservation-fk")
    ReservationDto getReservationDtoByReservationFk(@RequestParam long reservationFk);



}
