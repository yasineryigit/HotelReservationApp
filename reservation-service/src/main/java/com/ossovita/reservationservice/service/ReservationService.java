package com.ossovita.reservationservice.service;

import com.ossovita.commonservice.dto.ReservationDto;
import com.ossovita.reservationservice.payload.request.OnlineReservationRequest;
import com.ossovita.reservationservice.payload.response.OnlineReservationResponse;

import java.util.List;

public interface ReservationService {

    OnlineReservationResponse createOnlineReservation(OnlineReservationRequest onlineReservationRequest) throws Exception;

    boolean isReservationAvailable(long reservationFk);

    ReservationDto getReservationDtoByReservationFk(long reservationFk);

    List<ReservationDto> getReservationDtoListByRoomFkList(List<Long> roomFks);
}
