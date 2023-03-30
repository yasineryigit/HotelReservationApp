package com.ossovita.accountingservice.core.entities.dto.request;

import lombok.Data;

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
