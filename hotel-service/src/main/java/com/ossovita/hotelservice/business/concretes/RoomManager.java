package com.ossovita.hotelservice.business.concretes;

import com.ossovita.commonservice.core.payload.request.UpdateRoomStatusRequest;
import com.ossovita.commonservice.core.enums.RoomStatus;
import com.ossovita.commonservice.core.utilities.error.exception.IdNotFoundException;
import com.ossovita.hotelservice.business.abstracts.RoomService;
import com.ossovita.hotelservice.core.dataAccess.RoomRepository;
import com.ossovita.hotelservice.core.entities.Room;
import com.ossovita.hotelservice.core.entities.dto.request.RoomRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RoomManager implements RoomService {


    ModelMapper modelMapper;
    RoomRepository roomRepository;

    public RoomManager(ModelMapper modelMapper, RoomRepository roomRepository) {
        this.modelMapper = modelMapper;
        this.roomRepository = roomRepository;
    }

    @Override
    public Room createRoom(RoomRequest roomRequest) {
        Room room = modelMapper.map(roomRequest, Room.class);
        return roomRepository.save(room);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public boolean isRoomAvailable(long roomPk) {
        return roomRepository.existsByRoomPk(roomPk);
    }

    @Override
    public double getRoomPriceWithRoomFk(long roomFk) {
        Room room = roomRepository.findById(roomFk)
                .orElseThrow(() -> new IdNotFoundException("Room not found with the given roomFk: " + roomFk));
        return room.getRoomPrice();
    }

    @Override
    public List<Room> getAvailableRoomsByHotelFk(long hotelFk) {
        return roomRepository.findByHotelFkAndRoomStatus(hotelFk, RoomStatus.AVAILABLE);
    }


    @KafkaListener(
            topics = "room-status-update",
            groupId = "foo",
            containerFactory = "updateRoomStatusRequestConcurrentKafkaListenerContainerFactory"//we need to assign containerFactory
    )
    public void updateRoomStatusByRoomFk(UpdateRoomStatusRequest updateRoomStatusRequest) {
        log.info("roomStatus Updated | UpdateRoomStatusRequest: " + updateRoomStatusRequest.toString());
        Room room = roomRepository.findById(updateRoomStatusRequest.getRoomFk())
                .orElseThrow(() -> new IdNotFoundException("Room not found with the given roomFk: " + updateRoomStatusRequest.getRoomFk()));
        room.setRoomStatus(updateRoomStatusRequest.getRoomStatus());
        roomRepository.save(room);
    }

}
