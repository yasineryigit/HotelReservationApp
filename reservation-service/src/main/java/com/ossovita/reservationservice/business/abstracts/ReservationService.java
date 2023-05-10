package com.ossovita.reservationservice.business.abstracts;

import com.ossovita.commonservice.core.dto.ReservationDto;
import com.ossovita.reservationservice.core.entities.Reservation;
import com.ossovita.reservationservice.core.dto.request.OnlineReservationRequest;

public interface ReservationService {
    Reservation createOnlineReservation(OnlineReservationRequest onlineReservationRequest) throws Exception;

    boolean isReservationAvailable(long reservationFk);

    ReservationDto getReservationDtoByReservationFk(long reservationFk);
}
