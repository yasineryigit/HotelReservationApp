package com.ossovita.reservationservice.payload.request;

import com.sun.istack.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OnlineReservationRequest {


    @NotNull
    private long customerFk;

    @NotNull
    private long roomFk;

    @NotNull
    private LocalDateTime reservationStartTime;

    @NotNull
    private LocalDateTime reservationEndTime;


}
