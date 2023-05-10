package com.ossovita.reservationservice.business.abstracts;

import com.ossovita.commonservice.core.dto.ReservationDto;
import com.ossovita.reservationservice.core.entities.Reservation;
import com.ossovita.reservationservice.core.entities.dto.request.ReservationRequest;

import java.util.Optional;

public interface ReservationService {
    Reservation createReservation(ReservationRequest reservationRequest) throws Exception;

    boolean isReservationAvailable(long reservationFk);

    ReservationDto getReservationDtoByReservationFk(long reservationFk);
}
