package com.ossovita.accountingservice.payload.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReservationPaymentRequest {

    @NotNull
    private long reservationFk;

    @NotNull
    private long customerFk;


}
