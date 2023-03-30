package com.ossovita.reservationservice.business.abstracts;

import com.ossovita.reservationservice.core.entities.Reservation;
import com.ossovita.reservationservice.core.entities.dto.request.ReservationRequest;

public interface ReservationService {
    Reservation createReservation(ReservationRequest reservationRequest) throws Exception;

    boolean isReservationAvailable(long reservationFk);
}
