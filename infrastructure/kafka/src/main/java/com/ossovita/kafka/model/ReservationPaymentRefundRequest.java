package com.ossovita.kafka.model;

import com.ossovita.commonservice.enums.ReservationPaymentRefundReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationPaymentRefundRequest {

    private long reservationPaymentPk;

    private ReservationPaymentRefundReason reservationPaymentRefundReason;

    private String message;


}
