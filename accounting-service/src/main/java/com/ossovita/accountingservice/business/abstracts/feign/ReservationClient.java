package com.ossovita.accountingservice.business.abstracts.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "reservation-service", url = "http://localhost:8888/api/1.0/reservation")
public interface ReservationClient {

    @GetMapping("/is-reservation-available")
    public boolean isReservationAvailable(@RequestParam long reservationFk);

}
