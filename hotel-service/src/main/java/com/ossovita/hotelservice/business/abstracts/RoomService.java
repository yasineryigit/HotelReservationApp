package com.ossovita.hotelservice.business.abstracts;

import com.ossovita.hotelservice.core.entities.Room;
import com.ossovita.commonservice.core.entities.dtos.request.RoomRequest;

import java.util.List;

public interface RoomService {

    Room createRoom(RoomRequest roomRequest);

    List<Room> getAllRooms();

    boolean isRoomAvailable(long roomPk);
}
