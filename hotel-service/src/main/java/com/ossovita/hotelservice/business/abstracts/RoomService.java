package com.ossovita.hotelservice.business.abstracts;

import com.ossovita.commonservice.core.dto.RoomDto;
import com.ossovita.hotelservice.core.entities.Room;
import com.ossovita.hotelservice.core.entities.dto.request.RoomRequest;

import java.util.List;

public interface RoomService {

    Room createRoom(RoomRequest roomRequest);

    RoomDto getRoomDtoByRoomFk(long roomFk);

    List<Room> getAllRooms();

    List<Room> getAvailableRoomsByHotelFk(long hotelFk);

}
