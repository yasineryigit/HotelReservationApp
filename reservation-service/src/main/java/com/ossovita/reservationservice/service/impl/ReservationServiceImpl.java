package com.ossovita.reservationservice.service.impl;

import com.ossovita.clients.hotel.HotelClient;
import com.ossovita.clients.user.UserClient;
import com.ossovita.commonservice.dto.CustomerDto;
import com.ossovita.commonservice.dto.ReservationDto;
import com.ossovita.commonservice.dto.RoomDto;
import com.ossovita.commonservice.enums.ReservationStatus;
import com.ossovita.commonservice.enums.RoomStatus;
import com.ossovita.commonservice.exception.IdNotFoundException;
import com.ossovita.commonservice.exception.UnexpectedRequestException;
import com.ossovita.commonservice.payload.request.CheckRoomAvailabilityRequest;
import com.ossovita.reservationservice.dto.CheckingNotificationForCustomerDto;
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
import com.ossovita.reservationservice.producer.ReservationEventProducer;
import com.ossovita.reservationservice.repository.ReservationCheckingRepository;
import com.ossovita.reservationservice.repository.ReservationRepository;
import com.ossovita.reservationservice.service.OnlineReservationService;
import com.ossovita.reservationservice.service.ReservationService;
import com.ossovita.reservationservice.service.WalkInReservationService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    OnlineReservationService onlineReservationService;
    WalkInReservationService walkInReservationService;
    ReservationRepository reservationRepository;
    ReservationCheckingRepository reservationCheckingRepository;
    HotelClient hotelClient;
    UserClient userClient;
    ModelMapper modelMapper;
    ReservationEventProducer reservationEventProducer;

    public ReservationServiceImpl(ReservationRepository reservationRepository, OnlineReservationService onlineReservationService, WalkInReservationService walkInReservationService, ReservationCheckingRepository reservationCheckingRepository, HotelClient hotelClient, UserClient userClient, ModelMapper modelMapper, ReservationEventProducer reservationEventProducer) {
        this.reservationRepository = reservationRepository;
        this.onlineReservationService = onlineReservationService;
        this.walkInReservationService = walkInReservationService;
        this.reservationCheckingRepository = reservationCheckingRepository;
        this.hotelClient = hotelClient;
        this.userClient = userClient;
        this.modelMapper = modelMapper;
        this.reservationEventProducer = reservationEventProducer;
    }

    @Override
    public OnlineReservationResponse createOnlineReservation(OnlineReservationRequest onlineReservationRequest) {
        LocalDateTime now = LocalDateTime.now();

        //check no reservations can be made for past dates
        if (!onlineReservationRequest.getReservationStartTime().isAfter(now) || !onlineReservationRequest.getReservationEndTime().isAfter(now)) {
            throw new UnexpectedRequestException("No reservations can be made for past dates");
        }

        Reservation savedReservation = saveAndGetReservation(onlineReservationRequest.getRoomFk(),
                onlineReservationRequest.getCustomerFk(),
                onlineReservationRequest.getReservationStartTime(),
                onlineReservationRequest.getReservationEndTime());

        //also save OnlineReservation object to the database for completing relationship
        OnlineReservation onlineReservation = OnlineReservation.builder()
                .reservationFk(savedReservation.getReservationPk())
                .build();
        OnlineReservation savedOnlineReservation = onlineReservationService.save(onlineReservation);

        OnlineReservationResponse onlineReservationResponse = modelMapper.map(savedReservation, OnlineReservationResponse.class);
        onlineReservationResponse.setOnlineReservationFk(savedOnlineReservation.getOnlineReservationPk());

        return onlineReservationResponse;
    }

    @Override
    public WalkInReservationResponse createWalkInReservation(WalkInReservationRequest walkInReservationRequest) {

        Reservation savedReservation = saveAndGetReservation(walkInReservationRequest.getRoomFk(),
                walkInReservationRequest.getCustomerFk(),
                walkInReservationRequest.getReservationStartTime(),
                walkInReservationRequest.getReservationEndTime());

        WalkInReservation walkInReservation = WalkInReservation.builder()
                .reservationFk(savedReservation.getReservationPk())
                .employeeFk(walkInReservationRequest.getEmployeeFk())
                .build();

        WalkInReservation savedWalkInReservation = walkInReservationService.save(walkInReservation);

        WalkInReservationResponse walkInReservationResponse = modelMapper.map(savedReservation, WalkInReservationResponse.class);
        walkInReservationResponse.setWalkInReservationFk(savedWalkInReservation.getWalkInReservationPk());

        return walkInReservationResponse;
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
    public List<Reservation> getReservationsThatWillExpireWithInTheGivenDayLength(String interval) {
        return reservationRepository.getReservationsThatWillExpireWithInTheGivenDayLength(interval);
    }

    @Override
    public List<Reservation> saveAll(List<Reservation> reservationList) {
        return reservationRepository.saveAll(reservationList);
    }

    @Override
    public ReservationDto checkIn(ReservationCheckingRequest reservationCheckingRequest) {
        Reservation reservation = getReservation(reservationCheckingRequest.getReservationFk());
        CustomerDto customerDto = userClient.getCustomerDtoByCustomerPk(reservation.getCustomerFk());
        RoomDto roomDto = hotelClient.getRoomDtoByRoomPk(reservation.getRoomFk());

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

        //check room status
        if (!roomDto.getRoomStatus().equals(RoomStatus.AVAILABLE)) {
            throw new UnexpectedRequestException("Room is not available for check-in.");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkInTimeLimit = reservation.getReservationStartTime().minusHours(1);

        if (now.isAfter(checkInTimeLimit)) {
            //set reservation status
            reservation.setReservationStatus(ReservationStatus.CHECKED_IN);
            saveReservationChecking(reservationCheckingRequest, ReservationCheckingType.CHECK_IN);

            //set room status
            reservationEventProducer.sendRoomStatusUpdateEvent(reservation.getRoomFk(), RoomStatus.OCCUPIED);

            CheckingNotificationForCustomerDto checkingNotificationForCustomerDto = CheckingNotificationForCustomerDto.builder()
                    .customerEmail(customerDto.getCustomerEmail())
                    .customerFirstName(customerDto.getCustomerFirstName())
                    .customerLastName(customerDto.getCustomerLastName())
                    .reservationStartTime(reservation.getReservationStartTime())
                    .reservationEndTime(reservation.getReservationEndTime())
                    .hotelName(roomDto.getHotelName())
                    .roomNumber(roomDto.getRoomNumber())
                    .build();

            reservationEventProducer.sendCheckInNotificationToTheCustomer(checkingNotificationForCustomerDto);
            return modelMapper.map(reservationRepository.save(reservation), ReservationDto.class);

        } else {
            throw new UnexpectedRequestException("Check-in is not available at the moment. Please check-in at least 1 hour before the reservation start time.");
        }
    }

    @Override
    public ReservationDto checkOut(ReservationCheckingRequest reservationCheckingRequest) {
        Reservation reservation = getReservation(reservationCheckingRequest.getReservationFk());
        CustomerDto customerDto = userClient.getCustomerDtoByCustomerPk(reservation.getCustomerFk());
        RoomDto roomDto = hotelClient.getRoomDtoByRoomPk(reservation.getRoomFk());

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
            reservation.setReservationStatus(ReservationStatus.DEPARTED);
            saveReservationChecking(reservationCheckingRequest, ReservationCheckingType.CHECK_OUT);
            //set room status
            reservationEventProducer.sendRoomStatusUpdateEvent(reservation.getRoomFk(), RoomStatus.VACANT);
            CheckingNotificationForCustomerDto checkingNotificationForCustomerDto = CheckingNotificationForCustomerDto.builder()
                    .customerEmail(customerDto.getCustomerEmail())
                    .customerFirstName(customerDto.getCustomerFirstName())
                    .customerLastName(customerDto.getCustomerLastName())
                    .reservationStartTime(reservation.getReservationStartTime())
                    .reservationEndTime(reservation.getReservationEndTime())
                    .hotelName(roomDto.getHotelName())
                    .roomNumber(roomDto.getRoomNumber())
                    .build();
            reservationEventProducer.sendCheckOutNotificationToTheCustomer(checkingNotificationForCustomerDto);
            return modelMapper.map(reservationRepository.save(reservation), ReservationDto.class);

        } else {
            throw new UnexpectedRequestException("Check-out is not available at the moment. You can check-out after the reservation start time");
        }

    }

    @Override
    public void updateReservationAsBooked(Reservation reservation) {
        reservation.setReservationStatus(ReservationStatus.BOOKED);
        reservation.setReservationIsApproved(true);
        reservationRepository.save(reservation);
        log.info("updateReservationAsBooked {} : " + reservation.toString());
    }

    private void saveReservationChecking(ReservationCheckingRequest reservationCheckingRequest, ReservationCheckingType reservationCheckingType) {
        ReservationChecking reservationChecking = modelMapper.map(reservationCheckingRequest, ReservationChecking.class);
        reservationChecking.setReservationCheckingType(reservationCheckingType);
        reservationChecking.setReservationCheckingTime(LocalDateTime.now());
        reservationCheckingRepository.save(reservationChecking);
    }

    private Reservation saveAndGetReservation(long roomFk, long customerFk, LocalDateTime reservationStartTime, LocalDateTime reservationEndTime) {

        RoomDto roomDto = checkAndGetRoomDto(roomFk, customerFk, reservationStartTime, reservationEndTime);

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

    private RoomDto checkAndGetRoomDto(long roomFk, long customerFk, LocalDateTime reservationStartTime, LocalDateTime reservationEndTime) {
        //check endtime > starttime
        if (reservationEndTime.isBefore(reservationStartTime)) {
            throw new UnexpectedRequestException("Reservation end date must be later than start date");
        }

        //check reservation period
        long daysBetween = ChronoUnit.DAYS.between(reservationStartTime, reservationEndTime);
        if (daysBetween < 1) {
            throw new UnexpectedRequestException("Reservation period cannot be shorter than 1 day");
        }

        CheckRoomAvailabilityRequest checkRoomAvailabilityRequest = CheckRoomAvailabilityRequest
                .builder()
                .roomFk(roomFk)
                .reservationStartTime(reservationStartTime)
                .reservationEndTime(reservationEndTime)
                .build();

        //check room availability by given date range
        if (!isRoomAvailableByGivenDateRange(checkRoomAvailabilityRequest)) {
            throw new UnexpectedRequestException("Room is not available by given date range");
        }

        //check customer availability
        boolean isCustomerAvailable = userClient.isCustomerAvailable(customerFk);

        if (!isCustomerAvailable) {
            throw new IdNotFoundException("Customer not found by given id");
        }

        return hotelClient.getRoomDtoByRoomPk(checkRoomAvailabilityRequest.getRoomFk());
    }

    @Override
    public Reservation getReservation(long reservationFk) {
        return reservationRepository.findById(reservationFk)
                .orElseThrow(() -> new IdNotFoundException("Reservation not found with the given reservationFk: " + reservationFk));
    }


}
