package com.ossovita.hotelservice.business.abstracts;

import com.ossovita.commonservice.core.entities.Room;
import com.ossovita.commonservice.core.entities.dtos.RoomSaveFormDto;

import java.util.List;

public interface RoomService {

    Room createRoom(RoomSaveFormDto roomSaveFormDto);

    List<Room> getAllRooms();
}
