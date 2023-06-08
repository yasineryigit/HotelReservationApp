package com.ossovita.hotelservice.controller;

import com.ossovita.commonservice.dto.RoomDto;
import com.ossovita.hotelservice.entity.Room;
import com.ossovita.hotelservice.payload.request.AvailableRoomsByDateRangeAndCityRequest;
import com.ossovita.hotelservice.payload.request.RoomRequest;
import com.ossovita.hotelservice.service.RoomService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/1.0/hotel/rooms")
@Valid
public class RoomController {

    RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/create-room")
    public Room createRoom(@RequestBody RoomRequest roomRequest) {
        return roomService.createRoom(roomRequest);
    }

    @GetMapping("/get-all-rooms")
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/get-available-rooms-by-hotelfk")
    public List<Room> getAvailableRoomsByHotelFk(@RequestParam long hotelFk) {
        return roomService.getAvailableRoomsByHotelFk(hotelFk);
    }

    @PostMapping("/get-available-rooms-by-date-range-and-city")
    public List<Room> getAvailableRoomsByDateRangeAndCity(@RequestBody AvailableRoomsByDateRangeAndCityRequest availableRoomsByDateRangeAndCityRequest) {
        return roomService.getAvailableRoomsByDateRangeAndCity(availableRoomsByDateRangeAndCityRequest);
    }

    @GetMapping("/get-room-dto-by-room-pk")
    public RoomDto getRoomDtoByRoomFk(@RequestParam long roomPk) {
        return roomService.getRoomDtoByRoomPk(roomPk);
    }


}
