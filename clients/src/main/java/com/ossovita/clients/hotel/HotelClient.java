package com.ossovita.clients.hotel;

import com.ossovita.commonservice.dto.RoomDto;
import com.ossovita.commonservice.payload.request.CheckRoomAvailabilityRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "hotel-service", url = "http://localhost:8888/api/1.0/hotel")
public interface HotelClient {

    @GetMapping("/rooms/get-room-dto-by-room-fk")
    RoomDto getRoomDtoWithRoomFk(@RequestParam long roomFk);

    @GetMapping("/get-room-dto-if-room-available")
    RoomDto getRoomDtoIfRoomAvailable(@RequestBody CheckRoomAvailabilityRequest checkRoomAvailabilityRequest);
}
