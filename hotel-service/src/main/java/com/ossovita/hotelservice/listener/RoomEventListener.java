package com.ossovita.hotelservice.listener;

import com.ossovita.hotelservice.entity.Room;
import com.ossovita.hotelservice.service.RoomService;
import com.ossovita.kafka.model.RoomStatusUpdateRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoomEventListener {

    RoomService roomService;

    public RoomEventListener(RoomService roomService) {
        this.roomService = roomService;
    }

    @KafkaListener(
            topics = "room-status-update-topic",
            groupId = "foo",
            containerFactory = "roomStatusUpdateRequestConcurrentKafkaListenerContainerFactory"//we need to assign containerFactory
    )
    public void consumeRoomStatusUpdateRequest(RoomStatusUpdateRequest roomStatusUpdateRequest) {
        Room room = roomService.getRoom(roomStatusUpdateRequest.getRoomFk());
        log.info("roomStatus Updated | UpdateRoomStatusRequest: " + roomStatusUpdateRequest);
        room.setRoomStatus(roomStatusUpdateRequest.getRoomStatus());
        roomService.save(room);
    }
}
