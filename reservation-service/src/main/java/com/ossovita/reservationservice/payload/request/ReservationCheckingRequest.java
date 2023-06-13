package com.ossovita.reservationservice.payload.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReservationCheckingRequest {

    @NotNull
    private long reservationFk;

    @NotNull
    private long employeeFk;


}
