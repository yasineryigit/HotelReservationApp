package com.ossovita.reservationservice.core.dto.request;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class OnlineReservationRequest {


    @NotNull
    private long customerFk;

    @NotNull
    private long roomFk;

    @NotNull
    private int reservationDayLength;


}
