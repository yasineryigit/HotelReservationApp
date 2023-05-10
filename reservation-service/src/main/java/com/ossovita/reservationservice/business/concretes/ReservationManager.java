package com.ossovita.reservationservice.business.concretes;

import com.ossovita.commonservice.core.dto.ReservationDto;
import com.ossovita.commonservice.core.dto.RoomDto;
import com.ossovita.commonservice.core.enums.ReservationPaymentStatus;
import com.ossovita.commonservice.core.enums.RoomStatus;
import com.ossovita.commonservice.core.kafka.model.ReservationPaymentResponse;
import com.ossovita.commonservice.core.kafka.model.RoomStatusUpdateRequest;
import com.ossovita.commonservice.core.utilities.error.exception.IdNotFoundException;
import com.ossovita.commonservice.core.utilities.error.exception.RoomNotAvailableException;
import com.ossovita.reservationservice.business.abstracts.ReservationService;
import com.ossovita.reservationservice.business.abstracts.feign.HotelClient;
import com.ossovita.reservationservice.business.abstracts.feign.UserClient;
import com.ossovita.reservationservice.core.dataAccess.OnlineReservationRepository;
import com.ossovita.reservationservice.core.dataAccess.ReservationRepository;
import com.ossovita.reservationservice.core.dto.request.OnlineReservationRequest;
import com.ossovita.reservationservice.core.entities.OnlineReservation;
import com.ossovita.reservationservice.core.entities.Reservation;
import com.ossovita.reservationservice.core.enums.ReservationStatus;
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
    OnlineReservationRepository onlineReservationRepository;
    HotelClient hotelClient;
    UserClient userClient;
    ModelMapper modelMapper;
    KafkaTemplate<String, Object> kafkaTemplate;

    public ReservationManager(ReservationRepository reservationRepository, OnlineReservationRepository onlineReservationRepository, HotelClient hotelClient, UserClient userClient, ModelMapper modelMapper, KafkaTemplate<String, Object> kafkaTemplate) {
        this.reservationRepository = reservationRepository;
        this.onlineReservationRepository = onlineReservationRepository;
        this.hotelClient = hotelClient;
        this.userClient = userClient;
        this.modelMapper = modelMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Reservation createOnlineReservation(OnlineReservationRequest onlineReservationRequest) throws Exception {
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
                reservation.setReservationDayLength(onlineReservationRequest.getReservationDayLength());

                reservation.setReservationStatus(ReservationStatus.CREATED);

                //assign reservationPrice
                reservation.setReservationPrice(roomDto.getRoomPrice() * onlineReservationRequest.getReservationDayLength());

                Reservation savedReservation = reservationRepository.save(reservation);
                //also save OnlineReservation object to the database for completing relationship
                OnlineReservation onlineReservation = OnlineReservation.builder()
                        .reservationFk(savedReservation.getReservationPk())
                        .build();
                onlineReservationRepository.save(onlineReservation);

                return savedReservation;
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
