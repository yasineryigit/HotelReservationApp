package com.ossovita.commonservice.core.entities.dtos.request;

import com.ossovita.commonservice.core.entities.enums.ReservationPaymentStatus;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ReservationPaymentRequest {

    @NotBlank
    private ReservationPaymentStatus reservationPaymentStatus;

    @NotBlank
    private String reservationPaymentType;

    @NotNull
    private int reservationPaymentAmount;

    @NotNull
    private long reservationFk;
}
