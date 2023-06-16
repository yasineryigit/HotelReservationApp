package com.ossovita.reservationservice.service.impl;

import com.ossovita.clients.hotel.HotelClient;
import com.ossovita.clients.user.UserClient;
import com.ossovita.commonservice.dto.ReservationDto;
import com.ossovita.commonservice.dto.RoomDto;
import com.ossovita.commonservice.enums.*;
import com.ossovita.commonservice.exception.IdNotFoundException;
import com.ossovita.commonservice.exception.UnexpectedRequestException;
import com.ossovita.commonservice.payload.request.CheckRoomAvailabilityRequest;
import com.ossovita.kafka.model.NotificationRequest;
import com.ossovita.kafka.model.ReservationPaymentRefundRequest;
import com.ossovita.kafka.model.ReservationPaymentResponse;
import com.ossovita.kafka.model.RoomStatusUpdateRequest;
import com.ossovita.commonservice.dto.notification.ReservationBookedNotificationForCustomerDto;
import com.ossovita.reservationservice.entity.OnlineReservation;
import com.ossovita.reservationservice.entity.Reservation;
import com.ossovita.reservationservice.entity.ReservationChecking;
import com.ossovita.reservationservice.entity.WalkInReservation;
import com.ossovita.reservationservice.enums.ReservationCheckingType;
import com.ossovita.reservationservice.payload.request.OnlineReservationRequest;
import com.ossovita.reservationservice.payload.request.ReservationCheckingRequest;
import com.ossovita.reservationservice.payload.request.WalkInReservationRequest;
import com.ossovita.reservationservice.payload.response.OnlineReservationResponse;
import com.ossovita.reservationservice.payload.response.WalkInReservationResponse;
import com.ossovita.reservationservice.repository.OnlineReservationRepository;
import com.ossovita.reservationservice.repository.ReservationCheckingRepository;
import com.ossovita.reservationservice.repository.ReservationRepository;
import com.ossovita.reservationservice.repository.WalkInReservationRepository;
import com.ossovita.reservationservice.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    ReservationRepository reservationRepository;
    OnlineReservationRepository onlineReservationRepository;
    WalkInReservationRepository walkInReservationRepository;
    ReservationCheckingRepository reservationCheckingRepository;
    HotelClient hotelClient;
    UserClient userClient;
    ModelMapper modelMapper;
    KafkaTemplate<String, Object> kafkaTemplate;

    public ReservationServiceImpl(ReservationRepository reservationRepository, OnlineReservationRepository onlineReservationRepository, WalkInReservationRepository walkInReservationRepository, ReservationCheckingRepository reservationCheckingRepository, HotelClient hotelClient, UserClient userClient, ModelMapper modelMapper, KafkaTemplate<String, Object> kafkaTemplate) {
        this.reservationRepository = reservationRepository;
        this.onlineReservationRepository = onlineReservationRepository;
        this.walkInReservationRepository = walkInReservationRepository;
        this.reservationCheckingRepository = reservationCheckingRepository;
        this.hotelClient = hotelClient;
        this.userClient = userClient;
        this.modelMapper = modelMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public OnlineReservationResponse createOnlineReservation(OnlineReservationRequest onlineReservationRequest) {

        Reservation savedReservation = createReservation(onlineReservationRequest.getRoomFk(),
                onlineReservationRequest.getCustomerFk(),
                onlineReservationRequest.getReservationStartTime(),
                onlineReservationRequest.getReservationEndTime());

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
    public WalkInReservationResponse createWalkInReservation(WalkInReservationRequest walkInReservationRequest) {

        Reservation savedReservation = createReservation(walkInReservationRequest.getRoomFk(),
                walkInReservationRequest.getCustomerFk(),
                walkInReservationRequest.getReservationStartTime(),
                walkInReservationRequest.getReservationEndTime());

        WalkInReservation walkInReservation = WalkInReservation.builder()
                .reservationFk(savedReservation.getReservationPk())
                .employeeFk(walkInReservationRequest.getEmployeeFk())
                .build();

        WalkInReservation savedWalkInReservation = walkInReservationRepository.save(walkInReservation);

        WalkInReservationResponse walkInReservationResponse = modelMapper.map(savedReservation, WalkInReservationResponse.class);
        walkInReservationResponse.setWalkInReservationFk(savedWalkInReservation.getWalkInReservationPk());

        return walkInReservationResponse;
    }

    private Reservation createReservation(long roomFk, long customerFk, LocalDateTime reservationStartTime, LocalDateTime reservationEndTime) {
        CheckRoomAvailabilityRequest checkRoomAvailabilityRequest = CheckRoomAvailabilityRequest
                .builder()
                .roomFk(roomFk)
                .reservationStartTime(reservationStartTime)
                .reservationEndTime(reservationEndTime)
                .build();

        if (!isRoomAvailableByGivenDateRange(checkRoomAvailabilityRequest)) {
            throw new UnexpectedRequestException("Room is not available by given date range");
        }

        RoomDto roomDto = hotelClient.getRoomDtoWithRoomPk(checkRoomAvailabilityRequest.getRoomFk());

        boolean isCustomerAvailable = userClient.isCustomerAvailable(customerFk);

        if (!isCustomerAvailable) {
            throw new IdNotFoundException("Customer not found by given id");
        }

        //assign customerFk, roomFk
        Reservation reservation = Reservation.builder()
                .customerFk(customerFk)
                .roomFk(roomFk)
                .reservationStartTime(reservationStartTime)
                .reservationEndTime(reservationEndTime)
                .build();

        //assign reservationIsApproved
        reservation.setReservationIsApproved(false);

        //assign reservationTime
        reservation.setReservationCreateTime(LocalDateTime.now());

        //assign reservationStartTime
        reservation.setReservationStartTime(reservationStartTime);

        //assign reservationEndTime
        reservation.setReservationEndTime(reservationEndTime);

        reservation.setReservationStatus(ReservationStatus.CREATED);

        //assign reservationPrice
        reservation.setReservationPrice(roomDto.getRoomPrice().multiply(BigDecimal.valueOf(Duration.between(reservationStartTime, reservationEndTime).toDays())));

        //assign reservationPriceCurrency
        reservation.setReservationPriceCurrency(roomDto.getRoomPriceCurrency());

        return reservationRepository.save(reservation);
    }

    @Override
    public boolean isReservationAvailable(long reservationFk) {
        return reservationRepository.existsByReservationPk(reservationFk);
    }

    @Override
    public ReservationDto getReservationDtoByReservationFk(long reservationFk) {
        Reservation reservation = createReservation(reservationFk);
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
    public ReservationDto checkIn(ReservationCheckingRequest reservationCheckingRequest) {
        Reservation reservation = createReservation(reservationCheckingRequest.getReservationFk());

        //check employee
        if (!userClient.isEmployeeAvailable(reservationCheckingRequest.getEmployeeFk())) {
            throw new IdNotFoundException("Employee not found by given id");
        }

        //if already checked in, then throw error
        if (reservation.getReservationStatus().equals(ReservationStatus.CHECKED_IN)) {
            throw new UnexpectedRequestException("This reservation is already checked in.");
        }

        //if not booked, then throw error
        if (!reservation.getReservationStatus().equals(ReservationStatus.BOOKED)) {
            throw new UnexpectedRequestException("This reservation is not in booked status.");
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
            saveReservationChecking(reservationCheckingRequest, ReservationCheckingType.CHECK_IN);
            kafkaTemplate.send("room-status-update-topic", roomStatusUpdateRequest);
            //TODO: send notification to the customer (welcome, instructions(stay-over offer, discount))
            return modelMapper.map(reservationRepository.save(reservation), ReservationDto.class);

        } else {
            throw new UnexpectedRequestException("Check-in is not available at the moment. Please check-in at least 1 hour before the reservation start time.");
        }
    }


    public ReservationDto checkOut(ReservationCheckingRequest reservationCheckingRequest) {
        Reservation reservation = createReservation(reservationCheckingRequest.getReservationFk());

        //check employee
        if (!userClient.isEmployeeAvailable(reservationCheckingRequest.getEmployeeFk())) {
            throw new IdNotFoundException("Employee not found by given id");
        }

        //if reservation is not checked in, then throw error
        if (!reservation.getReservationStatus().equals(ReservationStatus.CHECKED_IN)) {
            throw new UnexpectedRequestException("This reservation is not checked in.");
        }

        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(reservation.getReservationStartTime())) {
            //set reservation status
            reservation.setReservationStatus(ReservationStatus.CHECKED_IN);
            //set room status
            RoomStatusUpdateRequest roomStatusUpdateRequest = RoomStatusUpdateRequest.builder()
                    .roomFk(reservation.getRoomFk())
                    .roomStatus(RoomStatus.VACANT)
                    .build();
            saveReservationChecking(reservationCheckingRequest, ReservationCheckingType.CHECK_OUT);
            kafkaTemplate.send("room-status-update-topic", roomStatusUpdateRequest);
            //TODO: send notification to the customer (feedback, instructions)
            return modelMapper.map(reservationRepository.save(reservation), ReservationDto.class);

        } else {
            throw new UnexpectedRequestException("Check-out is not available at the moment. You can check-out after the reservation start time");
        }

    }

    @KafkaListener(
            topics = "reservation-payment-response-topic",
            groupId = "foo",
            containerFactory = "reservationPaymentResponseKafkaListenerContainerFactory"//we need to assign containerFactory
    )
    public void listenReservationPaymentResponse(ReservationPaymentResponse reservationPaymentResponse) {
        log.info("Reservation Payment Updated | ReservationPaymentResponseModel: {}", reservationPaymentResponse.toString());
        Reservation reservationInDB = createReservation(reservationPaymentResponse.getReservationFk());
        CheckRoomAvailabilityRequest checkRoomAvailabilityRequest = CheckRoomAvailabilityRequest.builder()
                .roomFk(reservationInDB.getRoomFk())
                .reservationStartTime(reservationInDB.getReservationStartTime())
                .reservationEndTime(reservationInDB.getReservationEndTime())
                .build();

        if (reservationPaymentResponse.getPaymentStatus().equals(PaymentStatus.PAID)) {
            if (!isRoomAvailableByGivenDateRange(checkRoomAvailabilityRequest)) {//is room already booked
                handleDuplicateReservation(reservationPaymentResponse);
            } else {
                updateReservationAsBooked(reservationInDB);
                //send reservation completed email
                ReservationBookedNotificationForCustomerDto reservationBookedNotificationForCustomerDto = ReservationBookedNotificationForCustomerDto.builder()
                        .userEmail(reservationPaymentResponse.getCustomerEmail())
                        .userFirstName(reservationPaymentResponse.getCustomerFirstName())
                        .userLastName(reservationPaymentResponse.getCustomerLastName())
                        .reservationPk(reservationPaymentResponse.getReservationFk())
                        .reservationStartTime(reservationInDB.getReservationStartTime())
                        .reservationEndTime(reservationInDB.getReservationEndTime())
                        .reservationPrice(reservationInDB.getReservationPrice())
                        .reservationPriceCurrency(reservationInDB.getReservationPriceCurrency())
                        .build();
                sendReservationBookedNotificationToTheCustomer(reservationBookedNotificationForCustomerDto);
            }
        } else {
            // TODO: Handle ReservationPaymentStatus.FAILED case
        }
    }

    private void handleDuplicateReservation(ReservationPaymentResponse reservationPaymentResponse) {
        ReservationPaymentRefundRequest reservationPaymentRefundRequest = ReservationPaymentRefundRequest.builder()
                .reservationPaymentPk(reservationPaymentResponse.getReservationPaymentPk())
                .reservationPaymentRefundReason(ReservationPaymentRefundReason.DUPLICATE_RESERVATION)
                .message("Your balance has been refunded because the room you have booked was previously reserved by another user due to a system error.")
                .build();
        log.info("handleDuplicateReservation {} : " + reservationPaymentResponse.toString());
        kafkaTemplate.send("reservation-payment-refund-request-topic", reservationPaymentRefundRequest);
    }

    private void updateReservationAsBooked(Reservation reservation) {
        reservation.setReservationStatus(ReservationStatus.BOOKED);
        reservation.setReservationIsApproved(true);
        reservationRepository.save(reservation);
        log.info("updateReservationAsBooked {} : " + reservation.toString());
    }

    private void sendReservationBookedNotificationToTheCustomer(ReservationBookedNotificationForCustomerDto reservationBookedNotificationForCustomerDto) {
        RoomDto roomDto = hotelClient.getRoomDtoWithRoomPk(reservationBookedNotificationForCustomerDto.getReservationPk());
        HashMap<String, String> payload = new HashMap<>();

        payload.put("customer_email", reservationBookedNotificationForCustomerDto.getUserEmail());
        payload.put("customer_first_name", reservationBookedNotificationForCustomerDto.getUserFirstName());
        payload.put("customer_last_name", reservationBookedNotificationForCustomerDto.getUserLastName());
        payload.put("reservation_start_time", String.valueOf(reservationBookedNotificationForCustomerDto.getReservationStartTime()));
        payload.put("reservation_end_time", String.valueOf(reservationBookedNotificationForCustomerDto.getReservationEndTime()));
        payload.put("reservation_price", String.valueOf(reservationBookedNotificationForCustomerDto.getReservationPrice()));
        payload.put("reservation_price_currency", String.valueOf(reservationBookedNotificationForCustomerDto.getReservationPriceCurrency()));
        payload.put("hotel_name", roomDto.getHotelName());
        payload.put("room_number", String.valueOf(roomDto.getRoomNumber()));

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .to(reservationBookedNotificationForCustomerDto.getUserEmail())
                .payload(payload)
                .notificationType(NotificationType.CUSTOMER_WELCOME_NOTIFICATION)
                .build();
        kafkaTemplate.send("notification-request-topic", notificationRequest);
        log.info("Reservation booked notification sent to the customer {}: " + notificationRequest.toString());
    }

    private void saveReservationChecking(ReservationCheckingRequest reservationCheckingRequest, ReservationCheckingType reservationCheckingType) {
        ReservationChecking reservationChecking = modelMapper.map(reservationCheckingRequest, ReservationChecking.class);
        reservationChecking.setReservationCheckingType(reservationCheckingType);
        reservationCheckingRepository.save(reservationChecking);
    }

    private Reservation createReservation(long reservationFk) {
        return reservationRepository.findById(reservationFk)
                .orElseThrow(() -> new IdNotFoundException("Reservation not found with the given reservationFk: " + reservationFk));
    }


}
