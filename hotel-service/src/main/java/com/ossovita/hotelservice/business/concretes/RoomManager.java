package com.ossovita.hotelservice.business.concretes;

import com.ossovita.hotelservice.core.entities.dto.request.RoomRequest;
import com.ossovita.commonservice.core.utilities.error.exception.IdNotFoundException;
import com.ossovita.hotelservice.business.abstracts.RoomService;
import com.ossovita.hotelservice.core.dataAccess.RoomRepository;
import com.ossovita.hotelservice.core.entities.Room;
import com.ossovita.hotelservice.core.entities.enums.RoomStatus;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
    public int getRoomPriceWithRoomFk(long roomFk) {
        Room room = roomRepository.findById(roomFk)
                .orElseThrow(() -> new IdNotFoundException("Room not found with the given roomFk: " + roomFk));
        return room.getRoomPrice();
    }

    @Override
    public List<Room> getAvailableRoomsByHotelFk(long hotelFk) {
        return roomRepository.findByHotelFkAndRoomStatus(hotelFk, RoomStatus.AVAILABLE);
    }

}
