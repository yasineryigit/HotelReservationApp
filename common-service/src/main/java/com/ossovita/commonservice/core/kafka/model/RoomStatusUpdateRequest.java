package com.ossovita.commonservice.core.kafka.model;

import com.ossovita.commonservice.core.enums.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomStatusUpdateRequest {

    private long roomFk;

    private RoomStatus roomStatus;

    private long reservationPaymentFk;
}
