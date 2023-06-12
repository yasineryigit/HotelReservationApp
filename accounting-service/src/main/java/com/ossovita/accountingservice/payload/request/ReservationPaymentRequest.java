package com.ossovita.accountingservice.payload.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ReservationPaymentRequest {

    @NotNull
    private long reservationFk;

    @NotNull
    private long customerFk;

    @NotNull
    private long roomFk;

    @NotNull
    private LocalDateTime reservationStartTime;

    @NotNull
    private LocalDateTime reservationEndTime;


}
