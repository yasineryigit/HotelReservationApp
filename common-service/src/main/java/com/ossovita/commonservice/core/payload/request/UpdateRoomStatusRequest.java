package com.ossovita.commonservice.core.payload.request;

import com.ossovita.commonservice.core.enums.RoomStatus;
import lombok.Data;

@Data
public class UpdateRoomStatusRequest {

    private long roomFk;

    private RoomStatus roomStatus;
}
