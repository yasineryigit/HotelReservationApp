package com.ossovita.clients.reservation;

import com.ossovita.commonservice.dto.ReservationDto;
import com.ossovita.commonservice.payload.request.CheckRoomAvailabilityRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(value = "reservation-service", url = "http://localhost:8888/api/1.0/reservation")
public interface ReservationClient {

    @GetMapping("/get-reservation-dto-by-reservation-fk")
    ReservationDto getReservationDtoByReservationFk(@RequestParam long reservationFk);

    @GetMapping("/get-reservation-dto-list-by-room-fk-list")
    List<ReservationDto> getAllReservationsByRoomFkList(@RequestParam List<Long> roomFkList);

    @PostMapping("/is-room-available-by-given-date-range")
    boolean isRoomAvailableByGivenDateRange(@RequestBody CheckRoomAvailabilityRequest checkRoomAvailabilityRequest);

    @GetMapping("/get-not-available-room-fk-list-by-given-date-range")
    List<Long> getNotAvailableRoomFkListByGivenDateRange(@RequestParam List<Long> roomFkList, @RequestParam LocalDateTime requestStart, @RequestParam LocalDateTime requestEnd);
}
