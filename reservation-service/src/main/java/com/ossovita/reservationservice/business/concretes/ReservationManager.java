package com.ossovita.reservationservice.business.concretes;

import com.ossovita.reservationservice.business.abstracts.ReservationService;
import com.ossovita.reservationservice.business.abstracts.feign.HotelClient;
import com.ossovita.reservationservice.business.abstracts.feign.UserClient;
import com.ossovita.reservationservice.core.dataAccess.ReservationRepository;
import com.ossovita.reservationservice.core.entities.Reservation;
import com.ossovita.reservationservice.core.entities.dto.request.ReservationRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ReservationManager implements ReservationService {

    ReservationRepository reservationRepository;
    HotelClient hotelClient;
    UserClient userClient;
    ModelMapper modelMapper;


    public ReservationManager(ReservationRepository reservationRepository, HotelClient hotelClient, UserClient userClient, ModelMapper modelMapper) {
        this.reservationRepository = reservationRepository;
        this.hotelClient = hotelClient;
        this.userClient = userClient;
        this.modelMapper = modelMapper;
    }

    @Override
    public Reservation createReservation(ReservationRequest reservationRequest) throws Exception {
        //check from hotelclient | roomFk
        //check from userclient | employeeFk
        //check from userclient | customerFk
        if (hotelClient.isRoomAvailable(reservationRequest.getRoomFk())
                && userClient.isEmployeeAvailable(reservationRequest.getEmployeeFk())
                && userClient.isCustomerAvailable(reservationRequest.getCustomerFk())) {

            Reservation reservation = modelMapper.map(reservationRequest, Reservation.class);
            return reservationRepository.save(reservation);
        } else {
            throw new Exception("This request contains invalid id");
        }
    }

    @Override
    public boolean isReservationAvailable(long reservationFk) {
        return reservationRepository.existsByReservationPk(reservationFk);
    }




}
