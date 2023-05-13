package com.ossovita.reservationservice.service.impl;

import com.ossovita.clients.hotel.HotelClient;
import com.ossovita.clients.user.UserClient;
import com.ossovita.commonservice.dto.ReservationDto;
import com.ossovita.commonservice.dto.RoomDto;
import com.ossovita.commonservice.enums.ReservationPaymentStatus;
import com.ossovita.commonservice.enums.RoomStatus;
import com.ossovita.commonservice.exception.IdNotFoundException;
import com.ossovita.commonservice.exception.RoomNotAvailableException;
import com.ossovita.kafka.model.ReservationPaymentResponse;
import com.ossovita.kafka.model.RoomStatusUpdateRequest;
import com.ossovita.reservationservice.entity.OnlineReservation;
import com.ossovita.reservationservice.entity.Reservation;
import com.ossovita.reservationservice.enums.ReservationStatus;
import com.ossovita.reservationservice.payload.request.OnlineReservationRequest;
import com.ossovita.reservationservice.payload.response.OnlineReservationResponse;
import com.ossovita.reservationservice.repository.OnlineReservationRepository;
import com.ossovita.reservationservice.repository.ReservationRepository;
import com.ossovita.reservationservice.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    ReservationRepository reservationRepository;
    OnlineReservationRepository onlineReservationRepository;
    HotelClient hotelClient;
    UserClient userClient;
    ModelMapper modelMapper;
    KafkaTemplate<String, Object> kafkaTemplate;

    public ReservationServiceImpl(ReservationRepository reservationRepository, OnlineReservationRepository onlineReservationRepository, HotelClient hotelClient, UserClient userClient, ModelMapper modelMapper, KafkaTemplate<String, Object> kafkaTemplate) {
        this.reservationRepository = reservationRepository;
        this.onlineReservationRepository = onlineReservationRepository;
        this.hotelClient = hotelClient;
        this.userClient = userClient;
        this.modelMapper = modelMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public OnlineReservationResponse createOnlineReservation(OnlineReservationRequest onlineReservationRequest) {
        RoomDto roomDto = hotelClient.getRoomDtoWithRoomFk(onlineReservationRequest.getRoomFk());
        boolean isCustomerAvailable = userClient.isCustomerAvailable(onlineReservationRequest.getCustomerFk());

        if (roomDto != null && isCustomerAvailable) {//if roomDto & customer available by given ids
            if (roomDto.getRoomStatus().equals(RoomStatus.AVAILABLE)) {//if roomStatus available
                //assign customerFk, roomFk
                Reservation reservation = modelMapper.map(onlineReservationRequest, Reservation.class);

                //assign reservationIsApproved
                reservation.setReservationIsApproved(false);

                //assign reservationTime
                reservation.setReservationCreateTime(LocalDateTime.now());

                //assign reservationStartTime
                reservation.setReservationStartTime(onlineReservationRequest.getReservationStartTime());

                //assign reservationEndTime
                reservation.setReservationEndTime(onlineReservationRequest.getReservationEndTime());

                reservation.setReservationStatus(ReservationStatus.CREATED);

                //assign reservationPrice
                reservation.setReservationPrice(roomDto.getRoomPrice() * Duration.between(onlineReservationRequest.getReservationStartTime(), onlineReservationRequest.getReservationEndTime()).toDays());

                Reservation savedReservation = reservationRepository.save(reservation);
                //also save OnlineReservation object to the database for completing relationship
                OnlineReservation onlineReservation = OnlineReservation.builder()
                        .reservationFk(savedReservation.getReservationPk())
                        .build();
                OnlineReservation savedOnlineReservation = onlineReservationRepository.save(onlineReservation);

                OnlineReservationResponse onlineReservationResponse = modelMapper.map(savedReservation, OnlineReservationResponse.class);
                onlineReservationResponse.setOnlineReservationFk(savedOnlineReservation.getOnlineReservationPk());
                return onlineReservationResponse;
            } else {
                throw new RoomNotAvailableException("Selected room is not available.");
            }

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
            topics = "reservation-payment-update-response-topic",
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
            RoomStatusUpdateRequest roomStatusUpdateRequest = RoomStatusUpdateRequest.builder()
                    .roomFk(reservationInDB.getRoomFk())
                    .roomStatus(RoomStatus.RESERVED)
                    .reservationPaymentFk(reservationPaymentResponse.getReservationPaymentPk())
                    .build();
            kafkaTemplate.send("room-status-update-topic", roomStatusUpdateRequest);
        }//TODO | handle ReservationPaymentStatus.FAILED case
    }

    private Reservation getReservation(long reservationFk) {
        return reservationRepository.findById(reservationFk)
                .orElseThrow(() -> new IdNotFoundException("Reservation not found with the given reservationFk: " + reservationFk));
    }


}
