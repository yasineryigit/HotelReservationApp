package com.ossovita.hotelservice.service;

import com.ossovita.commonservice.dto.RoomDto;
import com.ossovita.hotelservice.entity.Room;
import com.ossovita.hotelservice.payload.request.AvailableRoomsByDateRangeAndCityRequest;
import com.ossovita.hotelservice.payload.request.RoomRequest;

import java.util.List;

public interface RoomService {

    Room createRoom(RoomRequest roomRequest);

    RoomDto getRoomDtoByRoomPk(long roomFk);

    List<Room> getAllRooms();

    List<Room> getAvailableRoomsByHotelFk(long hotelFk);

    List<Room> getAvailableRoomsByDateRangeAndCity(AvailableRoomsByDateRangeAndCityRequest availableRoomsByDateRangeAndCityRequest);

    Room getRoom(long roomFk);

    Room save(Room room);
}
