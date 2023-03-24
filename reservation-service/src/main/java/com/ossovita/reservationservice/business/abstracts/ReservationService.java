package com.ossovita.reservationservice.business.abstracts;

import com.ossovita.commonservice.core.entities.dtos.CreateReservationDto;

public interface ReservationService {
    CreateReservationDto createReservation(CreateReservationDto createReservationDto);
}
