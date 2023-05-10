package com.ossovita.hotelservice.business.abstracts;

import com.ossovita.hotelservice.core.entities.Room;
import com.ossovita.hotelservice.core.entities.dto.request.RoomRequest;

import java.util.List;

public interface RoomService {

    Room createRoom(RoomRequest roomRequest);

    List<Room> getAllRooms();

    boolean isRoomAvailable(long roomPk);

    double getRoomPriceWithRoomFk(long roomFk);

    List<Room> getAvailableRoomsByHotelFk(long hotelFk);


}
