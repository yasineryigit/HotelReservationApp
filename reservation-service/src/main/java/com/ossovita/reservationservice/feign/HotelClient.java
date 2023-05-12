package com.ossovita.reservationservice.feign;

import com.ossovita.commonservice.core.dto.RoomDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "hotel-service", url = "http://localhost:8888/api/1.0/hotel")
public interface HotelClient {

    @GetMapping("/rooms/get-room-dto-by-room-fk")
    RoomDto getRoomDtoWithRoomFk(@RequestParam long roomFk);

}
