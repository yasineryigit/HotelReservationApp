package com.ossovita.hotelservice.service.impl;

import com.ossovita.commonservice.core.dto.RoomDto;
import com.ossovita.commonservice.core.enums.ReservationPaymentRefundReason;
import com.ossovita.commonservice.core.enums.RoomStatus;
import com.ossovita.commonservice.core.kafka.model.ReservationPaymentRefundRequest;
import com.ossovita.commonservice.core.kafka.model.RoomStatusUpdateRequest;
import com.ossovita.commonservice.core.exception.IdNotFoundException;
import com.ossovita.hotelservice.service.RoomService;
import com.ossovita.hotelservice.repository.RoomRepository;
import com.ossovita.hotelservice.entity.Room;
import com.ossovita.hotelservice.payload.request.RoomRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RoomServiceImpl implements RoomService {


    ModelMapper modelMapper;
    RoomRepository roomRepository;
    KafkaTemplate<String, Object> kafkaTemplate;

    public RoomServiceImpl(ModelMapper modelMapper, RoomRepository roomRepository, KafkaTemplate<String, Object> kafkaTemplate) {
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
