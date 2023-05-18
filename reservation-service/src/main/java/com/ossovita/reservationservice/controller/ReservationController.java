package com.ossovita.reservationservice.controller;

import com.ossovita.commonservice.dto.ReservationDto;
import com.ossovita.reservationservice.payload.response.OnlineReservationResponse;
import com.ossovita.reservationservice.service.ReservationService;
import com.ossovita.reservationservice.payload.request.OnlineReservationRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/1.0/reservation")
public class ReservationController {

    ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/status")
    public String getStatus() {
        return "Working..";
    }

    @PostMapping("/create-online-reservation")
    public OnlineReservationResponse createOnlineReservation(@Valid @RequestBody OnlineReservationRequest onlineReservationRequest) throws Exception {
        return reservationService.createOnlineReservation(onlineReservationRequest);
    }

    @GetMapping("/is-reservation-available")
    public boolean isReservationAvailable(@RequestParam long reservationFk){
        return reservationService.isReservationAvailable(reservationFk);
    }

    @GetMapping("/get-reservation-dto-by-reservation-fk")
    public ReservationDto getReservationDtoByReservationFk(long reservationFk){
        return reservationService.getReservationDtoByReservationFk(reservationFk);
    }

    @GetMapping("/get-reservation-dto-list-by-room-fk-list")
    public List<ReservationDto> getReservationDtoListByRoomFkList(@RequestParam List<Long> roomFkList){
        return reservationService.getReservationDtoListByRoomFkList(roomFkList);
    }

}
