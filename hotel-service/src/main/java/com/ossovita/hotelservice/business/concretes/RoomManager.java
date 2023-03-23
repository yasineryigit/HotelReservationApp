package com.ossovita.hotelservice.business.concretes;

import com.ossovita.commonservice.core.dataAccess.RoomRepository;
import com.ossovita.commonservice.core.entities.Room;
import com.ossovita.commonservice.core.entities.dtos.RoomSaveFormDto;
import com.ossovita.hotelservice.business.abstracts.RoomService;
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
    public Room createRoom(RoomSaveFormDto roomSaveFormDto) {
        Room room = modelMapper.map(roomSaveFormDto, Room.class);
        return roomRepository.save(room);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
}
