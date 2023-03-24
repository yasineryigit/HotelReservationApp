package com.ossovita.reservationservice.business.concretes;

import com.ossovita.commonservice.core.dataAccess.ReservationRepository;
import com.ossovita.commonservice.core.entities.Reservation;
import com.ossovita.commonservice.core.entities.dtos.CreateReservationDto;
import com.ossovita.reservationservice.business.abstracts.ReservationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ReservationManager implements ReservationService {

    ModelMapper modelMapper;
    ReservationRepository reservationRepository;

    public ReservationManager(ModelMapper modelMapper, ReservationRepository reservationRepository) {
        this.modelMapper = modelMapper;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public CreateReservationDto createReservation(CreateReservationDto createReservationDto) {
        Reservation reservation = modelMapper.map(createReservationDto, Reservation.class);
        reservationRepository.save(reservation);
        return createReservationDto;
    }
}
