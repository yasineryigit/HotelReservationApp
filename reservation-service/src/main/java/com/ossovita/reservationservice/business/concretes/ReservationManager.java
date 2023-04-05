package com.ossovita.reservationservice.business.concretes;

import com.ossovita.commonservice.core.entities.dtos.request.ReservationPaymentRequest;
import com.ossovita.commonservice.core.utilities.error.exception.IdNotFoundException;
import com.ossovita.reservationservice.business.abstracts.ReservationService;
import com.ossovita.reservationservice.business.abstracts.feign.HotelClient;
import com.ossovita.reservationservice.business.abstracts.feign.UserClient;
import com.ossovita.reservationservice.core.dataAccess.ReservationRepository;
import com.ossovita.reservationservice.core.entities.Reservation;
import com.ossovita.reservationservice.core.entities.dto.request.ReservationRequest;
import com.ossovita.reservationservice.core.entities.enums.ReservationStatus;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
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
                && userClient.isCustomerAvailable(reservationRequest.getCustomerFk())) {

            //assign customerFk, roomFk
            Reservation reservation = modelMapper.map(reservationRequest, Reservation.class);

            //assign employeeFk if available
            if (reservationRequest.getEmployeeFk() != 0 && userClient.isEmployeeAvailable(reservationRequest.getEmployeeFk())) {
                reservation.setEmployeeFk(reservationRequest.getEmployeeFk());
            }
            //assign reservationStatus
            reservation.setReservationStatus(ReservationStatus.BOOKED);

            //assign reservationIsApproved
            reservation.setReservationIsApproved(false);

            //assign reservationTime
            reservation.setReservationTime(LocalDateTime.now());

            //assign reservationPrice
            reservation.setReservationPrice(hotelClient.getRoomPriceWithRoomFk(reservation.getRoomFk()));

            return reservationRepository.save(reservation);
        } else {
            throw new IdNotFoundException("This request contains invalid id");
        }
    }

    @Override
    public boolean isReservationAvailable(long reservationFk) {
        return reservationRepository.existsByReservationPk(reservationFk);
    }

    @KafkaListener(
            topics = "payment-update",
            groupId = "foo",
            containerFactory = "reservationPaymentRequestKafkaListenerContainerFactory"//we need to assign containerFactory
    )
    public void listenPaymentUpdate(ReservationPaymentRequest reservationPaymentRequest) {
        log.info("Payment Updated | ReservationPaymentRequest: " + reservationPaymentRequest.toString());
        Optional<Reservation> reservationInDB = reservationRepository.findById(reservationPaymentRequest.getReservationFk());
        reservationInDB.ifPresentOrElse(value -> {

            if (reservationPaymentRequest.getReservationPaymentStatus().equals("PAID")) {//if reservationPaymentStatus = true, then approve the reservation
                value.setReservationStatus(ReservationStatus.BOOKED);
                value.setReservationIsApproved(true);
            }
            reservationRepository.save(value);

        }, () -> log.info("ReservationManager | Payment Update Failed | ReservationPaymentRequest: " + reservationPaymentRequest.toString()));
    }


}
