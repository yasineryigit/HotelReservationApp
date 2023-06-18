package com.ossovita.hotelservice.service.impl;

import com.ossovita.clients.reservation.ReservationClient;
import com.ossovita.commonservice.dto.RoomDto;
import com.ossovita.commonservice.enums.ReservationPaymentRefundReason;
import com.ossovita.commonservice.enums.RoomStatus;
import com.ossovita.commonservice.exception.IdNotFoundException;
import com.ossovita.commonservice.exception.UnexpectedRequestException;
import com.ossovita.hotelservice.entity.Room;
import com.ossovita.hotelservice.payload.request.AvailableRoomsByDateRangeAndCityRequest;
import com.ossovita.hotelservice.payload.request.RoomRequest;
import com.ossovita.hotelservice.repository.RoomRepository;
import com.ossovita.hotelservice.service.RoomService;
import com.ossovita.kafka.model.ReservationPaymentRefundRequest;
import com.ossovita.kafka.model.RoomStatusUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    public RoomDto getRoomDtoByRoomPk(long roomPk) {
        Room room = getRoom(roomPk);
        RoomDto roomDto = modelMapper.map(room, RoomDto.class);
        roomDto.setHotelName(room.getHotel().getHotelName());
        return roomDto;
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
        //get rooms by city
        List<Room> roomsByCity = getRoomsByCity(availableRoomsByDateRangeAndCityRequest.getAddressCity());
        log.info("roomsByCity: " + roomsByCity.size());
        return getAvailableRoomsByGivenRoomListAndDateRange(roomsByCity, availableRoomsByDateRangeAndCityRequest.getReservationStartTime(), availableRoomsByDateRangeAndCityRequest.getReservationEndTime());
    }

    public List<Room> getAvailableRoomsByGivenRoomListAndDateRange(List<Room> roomList, LocalDateTime requestStart, LocalDateTime requestEnd) {

        LocalDateTime now = LocalDateTime.now();

        //check no reservations can be made for past dates
        if (!requestStart.isAfter(now) || !requestEnd.isAfter(now))  {
            throw new UnexpectedRequestException("No reservations can be made for past dates");
        }

        //check endtime > starttime
        if(requestEnd.isBefore(requestStart)){
            throw new UnexpectedRequestException("Reservation end date must be later than start date");
        }

        //check reservation period
        long daysBetween = ChronoUnit.DAYS.between(requestStart, requestEnd);
        if(daysBetween<1){
            throw new UnexpectedRequestException("Reservation period cannot be shorter than 1 day");
        }


        //assign roomPks into a list
        List<Long> roomPkList = roomList.stream().map(Room::getRoomPk).toList();
        log.info("roomPkList: " + roomList.size());

        //fetch reserved room fk list
        List<Long> notAvailableRoomFkListByGivenDateRange = reservationClient.getNotAvailableRoomFkListByGivenDateRange(roomPkList, requestStart, requestEnd);
        log.info("notAvailableRoomFkListByGivenDateRange: " + notAvailableRoomFkListByGivenDateRange.size());

        //return only the rooms which is available
        return roomList.stream()
                .filter(room -> !notAvailableRoomFkListByGivenDateRange.contains(room.getRoomPk())) // filter not available room fk list by date range
                .toList();
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
        log.info("roomStatus Updated | UpdateRoomStatusRequest: " + roomStatusUpdateRequest);
        room.setRoomStatus(roomStatusUpdateRequest.getRoomStatus());
        roomRepository.save(room);

    }


    private Room getRoom(long roomFk) {
        return roomRepository.findById(roomFk)
                .orElseThrow(() -> new IdNotFoundException("Room not found with the given roomFk: " + roomFk));
    }

}
