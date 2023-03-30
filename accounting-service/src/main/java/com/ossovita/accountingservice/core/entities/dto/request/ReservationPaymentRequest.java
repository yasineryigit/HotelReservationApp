package com.ossovita.accountingservice.core.entities.dto.request;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Data
public class ReservationPaymentRequest {

    @NotNull
    private String reservationPaymentStatus;

    @NotNull
    private String reservationPaymentType;

    @NotNull
    private int reservationPaymentAmount;

    @NotNull
    private long reservationFk;
}
