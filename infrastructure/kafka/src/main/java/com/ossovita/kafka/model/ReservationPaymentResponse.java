package com.ossovita.kafka.model;

import com.ossovita.commonservice.enums.ReservationPaymentStatus;
import com.ossovita.commonservice.enums.ReservationPaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationPaymentResponse {

    private long reservationPaymentPk;

    private ReservationPaymentStatus reservationPaymentStatus;

    private ReservationPaymentType reservationPaymentType;

    private double reservationPaymentAmount;

    private long reservationFk;



}
