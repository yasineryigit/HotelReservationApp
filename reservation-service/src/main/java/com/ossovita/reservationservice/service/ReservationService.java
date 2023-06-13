package com.ossovita.reservationservice.service;

import com.ossovita.commonservice.dto.ReservationDto;
import com.ossovita.commonservice.payload.request.CheckRoomAvailabilityRequest;
import com.ossovita.reservationservice.entity.Reservation;
import com.ossovita.reservationservice.payload.request.ReservationCheckingRequest;
import com.ossovita.reservationservice.payload.request.OnlineReservationRequest;
import com.ossovita.reservationservice.payload.response.OnlineReservationResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {

    OnlineReservationResponse createOnlineReservation(OnlineReservationRequest onlineReservationRequest);

    boolean isReservationAvailable(long reservationFk);

    ReservationDto getReservationDtoByReservationFk(long reservationFk);

    List<ReservationDto> getReservationDtoListByRoomFkList(List<Long> roomFks);

    boolean isRoomAvailableByGivenDateRange(CheckRoomAvailabilityRequest checkRoomAvailabilityRequest);

    List<Long> getNotAvailableRoomFkListByGivenDateRange(List<Long> roomFkList, LocalDateTime requestStart, LocalDateTime requestEnd);

    List<Reservation> getAllReservations();

    List<Reservation> saveAll(List<Reservation> reservationList);

    ReservationDto checkIn(ReservationCheckingRequest reservationCheckingRequest);

    ReservationDto checkOut(ReservationCheckingRequest reservationCheckingRequest);
}
