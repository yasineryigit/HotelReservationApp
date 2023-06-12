package com.ossovita.reservationservice.service.impl;

import com.ossovita.clients.hotel.HotelClient;
import com.ossovita.clients.user.UserClient;
import com.ossovita.commonservice.dto.ReservationDto;
import com.ossovita.commonservice.dto.RoomDto;
import com.ossovita.commonservice.enums.PaymentStatus;
import com.ossovita.commonservice.enums.ReservationPaymentRefundReason;
import com.ossovita.commonservice.enums.ReservationStatus;
import com.ossovita.commonservice.enums.RoomStatus;
import com.ossovita.commonservice.exception.IdNotFoundException;
import com.ossovita.commonservice.exception.UnexpectedRequestException;
import com.ossovita.commonservice.payload.request.CheckRoomAvailabilityRequest;
import com.ossovita.kafka.model.ReservationPaymentRefundRequest;
import com.ossovita.kafka.model.ReservationPaymentResponse;
import com.ossovita.kafka.model.RoomStatusUpdateRequest;
import com.ossovita.reservationservice.entity.OnlineReservation;
import com.ossovita.reservationservice.entity.Reservation;
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

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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

        CheckRoomAvailabilityRequest checkRoomAvailabilityRequest = CheckRoomAvailabilityRequest
                .builder()
                .roomFk(onlineReservationRequest.getRoomFk())
                .reservationStartTime(onlineReservationRequest.getReservationStartTime())
                .reservationEndTime(onlineReservationRequest.getReservationEndTime())
                .build();

        if (!isRoomAvailableByGivenDateRange(checkRoomAvailabilityRequest)) {
            throw new UnexpectedRequestException("Room is not available by given date range");
        }

        RoomDto roomDto = hotelClient.getRoomDtoWithRoomPk(checkRoomAvailabilityRequest.getRoomFk());

        boolean isCustomerAvailable = userClient.isCustomerAvailable(onlineReservationRequest.getCustomerFk());

        if (!isCustomerAvailable) {
            throw new IdNotFoundException("Customer not found by given id");
        }

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
        reservation.setReservationPrice(roomDto.getRoomPrice().multiply(BigDecimal.valueOf(Duration.between(onlineReservationRequest.getReservationStartTime(), onlineReservationRequest.getReservationEndTime()).toDays())));

        //assign reservationPriceCurrency
        reservation.setReservationPriceCurrency(roomDto.getRoomPriceCurrency());

        Reservation savedReservation = reservationRepository.save(reservation);

        //also save OnlineReservation object to the database for completing relationship
        OnlineReservation onlineReservation = OnlineReservation.builder()
                .reservationFk(savedReservation.getReservationPk())
                .build();
        OnlineReservation savedOnlineReservation = onlineReservationRepository.save(onlineReservation);

        OnlineReservationResponse onlineReservationResponse = modelMapper.map(savedReservation, OnlineReservationResponse.class);
        onlineReservationResponse.setOnlineReservationFk(savedOnlineReservation.getOnlineReservationPk());

        return onlineReservationResponse;
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

    @Override
    public List<ReservationDto> getReservationDtoListByRoomFkList(List<Long> roomFks) {
        List<Reservation> reservationList = reservationRepository.findByRoomFkIn(roomFks);
        return reservationList.stream()
                .map(reservation -> modelMapper.map(reservation, ReservationDto.class))
                .toList();
    }

    @Override
    public boolean isRoomAvailableByGivenDateRange(CheckRoomAvailabilityRequest checkRoomAvailabilityRequest) {
        return reservationRepository.isRoomAvailableByGivenDateRange(checkRoomAvailabilityRequest.getRoomFk(),
                checkRoomAvailabilityRequest.getReservationStartTime(),
                checkRoomAvailabilityRequest.getReservationEndTime());
    }

    @Override
    public List<Long> getNotAvailableRoomFkListByGivenDateRange(List<Long> roomFkList, LocalDateTime requestStart, LocalDateTime requestEnd) {
        //get reserved reservations by given date range
        return reservationRepository.getNotAvailableRoomFkListByGivenDateRange(roomFkList, requestStart, requestEnd);

    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Reservation> saveAll(List<Reservation> reservationList) {
        return reservationRepository.saveAll(reservationList);
    }

    @Override
    public ReservationDto checkIn(long reservationFk) {
        Reservation reservation = getReservation(reservationFk);

        if(reservation.getReservationStatus().equals(ReservationStatus.CHECKED_IN)){
            throw new UnexpectedRequestException("This reservation is already checked in.");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkInTimeLimit = reservation.getReservationStartTime().minusHours(1);

        if (now.isAfter(checkInTimeLimit)) {
            //set reservation status
            reservation.setReservationStatus(ReservationStatus.CHECKED_IN);
            //set room status
            RoomStatusUpdateRequest roomStatusUpdateRequest = RoomStatusUpdateRequest.builder()
                    .roomFk(reservation.getRoomFk())
                    .roomStatus(RoomStatus.OCCUPIED)
                    .build();
            //TODO: send notification to the customer (welcome, instructions)
            kafkaTemplate.send("room-status-update-topic", roomStatusUpdateRequest);
            return modelMapper.map(reservationRepository.save(reservation), ReservationDto.class);

        } else {
            throw new UnexpectedRequestException("Check-in is not available at the moment. Please check-in at least 1 hour before the reservation start time.");
        }
    }

    //TODO: add checkout method


    @KafkaListener(
            topics = "reservation-payment-response-topic",
            groupId = "foo",
            containerFactory = "reservationPaymentResponseKafkaListenerContainerFactory"//we need to assign containerFactory
    )
    public void listenReservationPaymentResponse(ReservationPaymentResponse reservationPaymentResponse) {
        log.info("Reservation Payment Updated | ReservationPaymentResponseModel: " + reservationPaymentResponse.toString());
        Reservation reservationInDB = getReservation(reservationPaymentResponse.getReservationFk());

        //double reservation payment response can occur on one room when reservation-payment-response-topic is overloaded, we need to handle duplicate reservation at a time
        //if reservation is already booked and reservation payment response equals PAID, rollback reservation payment
        //refund the balance
        if (reservationInDB.getReservationStatus().equals(ReservationStatus.BOOKED)) {
            ReservationPaymentRefundRequest reservationPaymentRefundRequest = ReservationPaymentRefundRequest.builder()
                    .reservationPaymentPk(reservationPaymentResponse.getReservationPaymentPk())
                    .reservationPaymentRefundReason(ReservationPaymentRefundReason.DUPLICATE_RESERVATION)
                    .message("Your balance has been refunded because the room you have booked was previously reserved by another user due to a system error.")
                    .build();
            kafkaTemplate.send("reservation-payment-refund-request-topic", reservationPaymentRefundRequest);
            return;
        }

        if (reservationPaymentResponse.getPaymentStatus().equals(PaymentStatus.PAID)) {//if reservationPaymentStatus = true, then approve the reservation
            reservationInDB.setReservationStatus(ReservationStatus.BOOKED);
            reservationInDB.setReservationIsApproved(true);
            reservationRepository.save(reservationInDB);

        } //TODO | handle ReservationPaymentStatus.FAILED case
    }

    private Reservation getReservation(long reservationFk) {
        return reservationRepository.findById(reservationFk)
                .orElseThrow(() -> new IdNotFoundException("Reservation not found with the given reservationFk: " + reservationFk));
    }


    /*
        //double RoomStatusUpdateRequest can occur when room-status-update-topic is overloaded, we need to handle duplicate reservation at a time


    * */

}
