package com.ossovita.hotelservice.service.impl;

import com.ossovita.clients.reservation.ReservationClient;
import com.ossovita.commonservice.dto.ReservationDto;
import com.ossovita.commonservice.dto.RoomDto;
import com.ossovita.commonservice.enums.ReservationPaymentRefundReason;
import com.ossovita.commonservice.enums.ReservationStatus;
import com.ossovita.commonservice.enums.RoomStatus;
import com.ossovita.commonservice.exception.IdNotFoundException;
import com.ossovita.commonservice.exception.RoomNotAvailableException;
import com.ossovita.commonservice.payload.request.CheckRoomAvailabilityRequest;
import com.ossovita.hotelservice.entity.Room;
import com.ossovita.hotelservice.payload.request.AvailableRoomsByDateRangeAndCityRequest;
import com.ossovita.hotelservice.payload.request.RoomRequest;
import com.ossovita.hotelservice.repository.RoomRepository;
import com.ossovita.hotelservice.service.RoomService;
import com.ossovita.kafka.model.ReservationPaymentRefundRequest;
import com.ossovita.kafka.model.RoomStatusUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class RoomServiceImpl implements RoomService {

    ReservationClient reservationClient;
    ModelMapper modelMapper;
    RoomRepository roomRepository;
    KafkaTemplate<String, Object> kafkaTemplate;

    public RoomServiceImpl(ReservationClient reservationClient, ModelMapper modelMapper, RoomRepository roomRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.reservationClient = reservationClient;
        this.modelMapper = modelMapper;
        this.roomRepository = roomRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Room createRoom(RoomRequest roomRequest) {
        Room room = modelMapper.map(roomRequest, Room.class);
        return roomRepository.save(room);
    }

    @Override
    public RoomDto getRoomDtoByRoomFk(long roomFk) {
        return modelMapper.map(getRoom(roomFk), RoomDto.class);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public List<Room> getAvailableRoomsByHotelFk(long hotelFk) {
        return roomRepository.findByHotelFkAndRoomStatus(hotelFk, RoomStatus.AVAILABLE);
    }

    @Override
    public List<Room> getAvailableRoomsByDateRangeAndCity(AvailableRoomsByDateRangeAndCityRequest availableRoomsByDateRangeAndCityRequest) {
        List<Room> roomsByCity = getRoomsByCity(availableRoomsByDateRangeAndCityRequest.getAddressCity());
        log.info("roomsByCity: " + roomsByCity.size());
        return getAvailableRoomsByGivenRoomListAndDateRange(roomsByCity, availableRoomsByDateRangeAndCityRequest.getReservationStartTime(), availableRoomsByDateRangeAndCityRequest.getReservationEndTime());
    }

    public boolean isRoomAvailable(CheckRoomAvailabilityRequest checkRoomAvailabilityRequest) {
        List<Room> availableRooms = getAvailableRoomsByGivenRoomListAndDateRange(List.of(getRoom(checkRoomAvailabilityRequest.getRoomFk())),
                checkRoomAvailabilityRequest.getReservationStartTime(),
                checkRoomAvailabilityRequest.getReservationEndTime());

        return availableRooms.stream()
                .anyMatch(room -> room.getRoomPk() == checkRoomAvailabilityRequest.getRoomFk());
    }

    @Override
    public RoomDto fetchRoomDtoIfRoomAvailable(CheckRoomAvailabilityRequest checkRoomAvailabilityRequest) {
        if (isRoomAvailable(checkRoomAvailabilityRequest)) {
            return modelMapper.map(getRoom(checkRoomAvailabilityRequest.getRoomFk()), RoomDto.class);
        } else {
            throw new RoomNotAvailableException("Room is not available by given date range");
        }
    }

    public List<Room> getAvailableRoomsByGivenRoomListAndDateRange(List<Room> roomList, LocalDateTime requestStart, LocalDateTime requestEnd) {        //getAllReservationsByRoomFks
        List<Long> roomPkList = roomList.stream().map(Room::getRoomPk).toList();
        List<ReservationDto> reservationDtoList = reservationClient.getAllReservationsByRoomFkList(roomPkList);
        log.info("reservationDtoList: " + reservationDtoList.size());

        List<ReservationDto> bookedReservationDtoListByGivenDateRange = fetchBookedReservationDtoListByGivenDateRange(requestStart, requestEnd, reservationDtoList);
        log.info("bookedReservationDtoListByGivenDateRange: " + bookedReservationDtoListByGivenDateRange.size());
        List<Long> reservedRoomFkListByGivenDateRange = bookedReservationDtoListByGivenDateRange.stream().map(ReservationDto::getRoomFk).toList();

        return roomList.stream()
                .filter(room -> !reservedRoomFkListByGivenDateRange.contains(room.getRoomPk())) // reservedRoomFkListByGivenDateRange'te olmayan Room nesnelerini filtrele
                .filter(room -> room.getRoomStatus().toString().equals(RoomStatus.AVAILABLE.toString()))
                .toList();
    }


    //refactor: filterReservationDtoListByGivenDateRangeAndRoomStatus(List<ReservationDto> reservationDtoList, LocalDateTime requestStart,LocalDateTime requestEnd, RoomStatus roomStatus)
    @NotNull
    private List<ReservationDto> fetchBookedReservationDtoListByGivenDateRange(LocalDateTime requestStart, LocalDateTime requestEnd, List<ReservationDto> reservationDtoList) {
        return reservationDtoList.stream()
                .filter(reservationDto -> {
                    LocalDateTime reservationStart = reservationDto.getReservationStartTime();
                    LocalDateTime reservationEnd = reservationDto.getReservationEndTime();

                    // çakışmıyorsa
                    boolean isNonOverlap = (reservationStart.isBefore(requestStart) && reservationEnd.isBefore(requestStart)
                            || reservationStart.isAfter(requestEnd) && reservationEnd.isAfter(requestEnd));

                    // çakışan tarihler arasında booked reservation varsa getir
                    return !isNonOverlap && reservationDto.toString().equals(ReservationStatus.BOOKED.toString());

                }).toList();
    }

    private List<Room> getRoomsByCity(String addressCity) {
        return roomRepository.findRoomsByHotel_Address_AddressCity(addressCity);
    }


    @KafkaListener(
            topics = "room-status-update-topic",
            groupId = "foo",
            containerFactory = "roomStatusUpdateRequestConcurrentKafkaListenerContainerFactory"//we need to assign containerFactory
    )
    public void consumeRoomStatusUpdateRequest(RoomStatusUpdateRequest roomStatusUpdateRequest) {
        Room room = getRoom(roomStatusUpdateRequest.getRoomFk());
        //double RoomStatusUpdateRequest can occur when room-status-update-topic is overloaded, we need to handle duplicate reservation at a time
        //if room is already reserved and roomStatusUpdateRequest equals RESERVED, rollback reservation payment
        if (room.getRoomStatus().equals(RoomStatus.RESERVED) && roomStatusUpdateRequest.getRoomStatus().equals(RoomStatus.RESERVED)) {
            //refund the balance
            ReservationPaymentRefundRequest reservationPaymentRefundRequest = ReservationPaymentRefundRequest.builder()
                    .reservationPaymentPk(roomStatusUpdateRequest.getReservationPaymentFk())
                    .reservationPaymentRefundReason(ReservationPaymentRefundReason.DUPLICATE_RESERVATION)
                    .message("Your balance has been refunded because the room you have booked was previously reserved by another user due to a system error.")//TODO | multi language
                    .build();
            kafkaTemplate.send("reservation-payment-refund-request-topic", reservationPaymentRefundRequest);
        } else {
            log.info("roomStatus Updated | UpdateRoomStatusRequest: " + roomStatusUpdateRequest);
            room.setRoomStatus(roomStatusUpdateRequest.getRoomStatus());
            roomRepository.save(room);
        }


    }


    private Room getRoom(long roomFk) {
        return roomRepository.findById(roomFk)
                .orElseThrow(() -> new IdNotFoundException("Room not found with the given roomFk: " + roomFk));
    }

}
