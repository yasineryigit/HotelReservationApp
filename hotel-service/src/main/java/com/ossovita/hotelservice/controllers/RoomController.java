package com.ossovita.hotelservice.controllers;

import com.ossovita.commonservice.core.entities.enums.RoomStatus;
import com.ossovita.hotelservice.business.abstracts.RoomService;
import com.ossovita.hotelservice.core.entities.Room;
import com.ossovita.hotelservice.core.entities.dto.request.RoomRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/1.0/hotel/rooms")
public class RoomController {

    RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/create-room")
    public Room createRoom(@Valid @RequestBody RoomRequest roomRequest) {
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

    @GetMapping("/is-room-available")
    public boolean isRoomAvailable(@RequestParam long roomPk) {
        return roomService.isRoomAvailable(roomPk);
    }

    @GetMapping("/get-room-price-with-roomfk")
    public int getRoomPriceWithRoomFk(@RequestParam long roomFk) {
        return roomService.getRoomPriceWithRoomFk(roomFk);
    }


}
