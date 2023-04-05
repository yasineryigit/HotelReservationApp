package com.ossovita.reservationservice.business.abstracts.feign;

import com.ossovita.commonservice.core.entities.enums.RoomStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "hotel-service", url = "http://localhost:8888/api/1.0/hotel")
public interface HotelClient {

    @GetMapping("/rooms/is-room-available")
    boolean isRoomAvailable(@RequestParam long roomPk);

    @GetMapping("/rooms/get-room-price-with-roomfk")
    int getRoomPriceWithRoomFk(@RequestParam long roomFk);

    @PutMapping("/rooms/set-room-status-by-roomfk")
    void setRoomStatusByRoomFk(RoomStatus roomStatus, long roomFk);
}
