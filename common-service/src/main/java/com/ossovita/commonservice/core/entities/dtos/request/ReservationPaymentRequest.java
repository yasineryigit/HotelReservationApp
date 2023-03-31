package com.ossovita.commonservice.core.entities.dtos.request;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ReservationPaymentRequest {

    @NotBlank
    private String reservationPaymentStatus;

    @NotBlank
    private String reservationPaymentType;

    @NotNull
    private int reservationPaymentAmount;

    @NotNull
    private long reservationFk;
}
