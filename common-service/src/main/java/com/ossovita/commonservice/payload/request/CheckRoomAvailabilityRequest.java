package com.ossovita.commonservice.payload.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CheckRoomAvailabilityRequest {

    private long roomFk;

    private LocalDateTime reservationStartTime;

    private LocalDateTime reservationEndTime;
}
