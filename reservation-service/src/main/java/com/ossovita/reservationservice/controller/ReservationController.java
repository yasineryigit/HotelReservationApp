package com.ossovita.reservationservice.controller;

import com.ossovita.commonservice.dto.ReservationDto;
import com.ossovita.commonservice.payload.request.CheckRoomAvailabilityRequest;
import com.ossovita.reservationservice.payload.request.ReservationCheckingRequest;
import com.ossovita.reservationservice.payload.request.OnlineReservationRequest;
import com.ossovita.reservationservice.payload.request.WalkInReservationRequest;
import com.ossovita.reservationservice.payload.response.OnlineReservationResponse;
import com.ossovita.reservationservice.payload.response.WalkInReservationResponse;
import com.ossovita.reservationservice.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
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
    public OnlineReservationResponse createOnlineReservation(@Valid @RequestBody OnlineReservationRequest onlineReservationRequest){
        return reservationService.createOnlineReservation(onlineReservationRequest);
    }

    @PostMapping("/create-walk-in-reservation")
    public WalkInReservationResponse createWalkInReservation(@Valid @RequestBody WalkInReservationRequest walkInReservationRequest){
        return reservationService.createWalkInReservation(walkInReservationRequest);
    }

    @GetMapping("/is-reservation-available")
    public boolean isReservationAvailable(@RequestParam long reservationFk) {
        return reservationService.isReservationAvailable(reservationFk);
    }

    @GetMapping("/get-reservation-dto-by-reservation-fk")
    public ReservationDto getReservationDtoByReservationFk(long reservationFk) {
        return reservationService.getReservationDtoByReservationFk(reservationFk);
    }

    @GetMapping("/get-reservation-dto-list-by-room-fk-list")
    public List<ReservationDto> getReservationDtoListByRoomFkList(@RequestParam List<Long> roomFkList) {
        return reservationService.getReservationDtoListByRoomFkList(roomFkList);
    }

    @GetMapping("/get-not-available-room-fk-list-by-given-date-range")
    List<Long> getNotAvailableRoomFkListByGivenDateRange(@RequestParam List<Long> roomFkList, @RequestParam LocalDateTime requestStart, @RequestParam LocalDateTime requestEnd) {
        return reservationService.getNotAvailableRoomFkListByGivenDateRange(roomFkList, requestStart, requestEnd);
    }

    @PostMapping("/is-room-available-by-given-date-range")
    public boolean isRoomAvailableByGivenDateRange(@RequestBody CheckRoomAvailabilityRequest checkRoomAvailabilityRequest){
        return reservationService.isRoomAvailableByGivenDateRange(checkRoomAvailabilityRequest);
    }

    @PutMapping("/check-in")
    public ReservationDto checkIn(@RequestBody ReservationCheckingRequest reservationCheckingRequest) {
        return reservationService.checkIn(reservationCheckingRequest);
    }

    @PutMapping("/check-out")
    public ReservationDto checkOut(@RequestBody ReservationCheckingRequest reservationCheckingRequest) {
        return reservationService.checkOut(reservationCheckingRequest);
    }




}
