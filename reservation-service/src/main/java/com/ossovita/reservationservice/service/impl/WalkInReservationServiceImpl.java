package com.ossovita.reservationservice.service.impl;

import com.ossovita.reservationservice.entity.WalkInReservation;
import com.ossovita.reservationservice.repository.WalkInReservationRepository;
import com.ossovita.reservationservice.service.OnlineReservationService;
import com.ossovita.reservationservice.service.WalkInReservationService;
import org.springframework.stereotype.Service;

@Service
public class WalkInReservationServiceImpl implements WalkInReservationService {


    WalkInReservationRepository walkInReservationRepository;

    public WalkInReservationServiceImpl(WalkInReservationRepository walkInReservationRepository) {
        this.walkInReservationRepository = walkInReservationRepository;
    }

    @Override
    public WalkInReservation save(WalkInReservation walkInReservation) {
        return walkInReservationRepository.save(walkInReservation);
    }
}
