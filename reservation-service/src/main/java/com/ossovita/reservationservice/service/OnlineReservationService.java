package com.ossovita.reservationservice.service;

import com.ossovita.reservationservice.entity.OnlineReservation;
import com.ossovita.reservationservice.repository.OnlineReservationRepository;

public interface OnlineReservationService {

    OnlineReservation save(OnlineReservation onlineReservation);
}
