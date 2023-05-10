package com.ossovita.reservationservice.business.concretes;

import com.ossovita.commonservice.core.dto.ReservationDto;
import com.ossovita.commonservice.core.enums.ReservationPaymentStatus;
import com.ossovita.commonservice.core.enums.RoomStatus;
import com.ossovita.commonservice.core.kafka.model.ReservationPaymentResponse;
import com.ossovita.commonservice.core.payload.request.UpdateRoomStatusRequest;
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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class ReservationManager implements ReservationService {

    ReservationRepository reservationRepository;
    HotelClient hotelClient;
    UserClient userClient;
    ModelMapper modelMapper;
    KafkaTemplate<String, Object> kafkaTemplate;


    public ReservationManager(ReservationRepository reservationRepository, HotelClient hotelClient, UserClient userClient, ModelMapper modelMapper, KafkaTemplate<String, Object> kafkaTemplate) {
        this.reservationRepository = reservationRepository;
        this.hotelClient = hotelClient;
        this.userClient = userClient;
        this.modelMapper = modelMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Reservation createReservation(ReservationRequest reservationRequest) throws Exception {
        //TODO | refactor | replace hotelclient methods with getRoomByRoomFk method
        if (hotelClient.isRoomAvailable(reservationRequest.getRoomFk())
                && userClient.isCustomerAvailable(reservationRequest.getCustomerFk())) {

            //assign customerFk, roomFk
            Reservation reservation = modelMapper.map(reservationRequest, Reservation.class);

            //assign employeeFk if available//TODO | throw error if invalid employeeFk provided
            if (reservationRequest.getEmployeeFk() != 0 && userClient.isEmployeeAvailable(reservationRequest.getEmployeeFk())) {
                reservation.setEmployeeFk(reservationRequest.getEmployeeFk());
            }

            //assign reservationIsApproved
            reservation.setReservationIsApproved(false);

            //assign reservationTime
            reservation.setReservationCreateTime(LocalDateTime.now());

            //assign reservationStartTime
            reservation.setReservationDayLength(reservationRequest.getReservationDayLength());

            reservation.setReservationStatus(ReservationStatus.CREATED);

            //assign reservationPrice
            reservation.setReservationPrice(hotelClient.getRoomPriceWithRoomFk(reservation.getRoomFk()) * reservationRequest.getReservationDayLength());

            //TODO | create-reservation event fill be fired & it will consumed by hotel-service to update its room status for PENDING_PAYMENT


            return reservationRepository.save(reservation);
        } else {
            throw new IdNotFoundException("This request contains invalid id");
        }
    }

    @Override
    public boolean isReservationAvailable(long reservationFk) {
        return reservationRepository.existsByReservationPk(reservationFk);
    }

    @Override
    public ReservationDto getReservationDtoByReservationFk(long reservationFk) {
        Reservation reservation = getReservation(reservationFk);
        return modelMapper.map(reservation, ReservationDto.class);
    }

    @KafkaListener(
            topics = "reservation-payment-update",
            groupId = "foo",
            containerFactory = "reservationPaymentResponseKafkaListenerContainerFactory"//we need to assign containerFactory
    )
    public void listenPaymentUpdate(ReservationPaymentResponse reservationPaymentResponse) {
        log.info("Payment Updated | ReservationPaymentResponseModel: " + reservationPaymentResponse.toString());
        Reservation reservationInDB = getReservation(reservationPaymentResponse.getReservationFk());

        if (reservationPaymentResponse.getReservationPaymentStatus().equals(ReservationPaymentStatus.PAID)) {//if reservationPaymentStatus = true, then approve the reservation
            reservationInDB.setReservationStatus(ReservationStatus.BOOKED);
            reservationInDB.setReservationIsApproved(true);

            reservationRepository.save(reservationInDB);

            //change roomStatus
            UpdateRoomStatusRequest updateRoomStatusRequest = new UpdateRoomStatusRequest();
            updateRoomStatusRequest.setRoomFk(reservationInDB.getRoomFk());
            updateRoomStatusRequest.setRoomStatus(RoomStatus.RESERVED);
            kafkaTemplate.send("room-status-update", updateRoomStatusRequest);
        }//TODO | handle ReservationPaymentStatus.FAILED case
    }

    private Reservation getReservation(long reservationFk) {
        return reservationRepository.findById(reservationFk)
                .orElseThrow(() -> new IdNotFoundException("Room not found with the given roomFk: " + reservationFk));
    }


}
