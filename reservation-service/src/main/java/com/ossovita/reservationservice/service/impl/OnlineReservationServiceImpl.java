package com.ossovita.reservationservice.service.impl;

import com.ossovita.reservationservice.entity.OnlineReservation;
import com.ossovita.reservationservice.repository.OnlineReservationRepository;
import com.ossovita.reservationservice.service.OnlineReservationService;
import org.springframework.stereotype.Service;

@Service
public class OnlineReservationServiceImpl implements OnlineReservationService {

    OnlineReservationRepository onlineReservationRepository;

    public OnlineReservationServiceImpl(OnlineReservationRepository onlineReservationRepository) {
        this.onlineReservationRepository = onlineReservationRepository;
    }


    @Override
    public OnlineReservation save(OnlineReservation onlineReservation) {
        return onlineReservationRepository.save(onlineReservation);
    }
}
