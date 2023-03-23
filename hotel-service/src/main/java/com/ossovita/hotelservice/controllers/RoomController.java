package com.ossovita.hotelservice.controllers;

import com.ossovita.commonservice.core.entities.Room;
import com.ossovita.commonservice.core.entities.dtos.RoomSaveFormDto;
import com.ossovita.hotelservice.business.abstracts.RoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/1.0/hotel/rooms")
public class RoomController {

    RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/create-room")
    public Room createRoom(@RequestBody RoomSaveFormDto roomSaveFormDto) {
        return roomService.createRoom(roomSaveFormDto);
    }

    @GetMapping("/get-all-rooms")
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }
}
