package com.ossovita.reservationservice.core.entities.dto.request;

import com.sun.istack.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationRequest {


    private long employeeFk;

    @NotNull
    private long customerFk;

    @NotNull
    private long roomFk;

    @NotNull
    private int reservationDayLength;


}
