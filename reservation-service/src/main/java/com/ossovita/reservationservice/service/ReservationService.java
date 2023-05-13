package com.ossovita.reservationservice.service;

import com.ossovita.commonservice.dto.ReservationDto;
import com.ossovita.reservationservice.entity.Reservation;
import com.ossovita.reservationservice.payload.request.OnlineReservationRequest;

public interface ReservationService {
    Reservation createOnlineReservation(OnlineReservationRequest onlineReservationRequest) throws Exception;

    boolean isReservationAvailable(long reservationFk);

    ReservationDto getReservationDtoByReservationFk(long reservationFk);
}
