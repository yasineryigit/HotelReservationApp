package com.ossovita.kafka.model;

import com.ossovita.commonservice.enums.RoomStatus;
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
