package com.ossovita.kafka.model;

import com.ossovita.commonservice.enums.PaymentStatus;
import com.ossovita.commonservice.enums.ReservationPaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationPaymentResponse {

    private long reservationPaymentPk;

    private PaymentStatus paymentStatus;

    private ReservationPaymentType reservationPaymentType;//TODO: remove

    private BigDecimal reservationPaymentAmount;

    private long reservationFk;



}
