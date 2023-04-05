package com.ossovita.commonservice.core.entities.dtos.request;

import com.ossovita.commonservice.core.entities.enums.RoomStatus;
import lombok.Builder;
import lombok.Data;

@Data
public class UpdateRoomStatusRequest {

    private long roomFk;

    private RoomStatus roomStatus;
}
