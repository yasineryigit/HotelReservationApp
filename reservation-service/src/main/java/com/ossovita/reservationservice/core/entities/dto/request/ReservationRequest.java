package com.ossovita.reservationservice.core.entities.dto.request;

import com.sun.istack.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationRequest {

    @NotNull
    private long employeeFk;

    @NotNull
    private long customerFk;

    @NotNull
    private long roomFk;

    @NotNull
    private LocalDateTime reservationTime;

    @NotNull
    private int reservationPrice;

    @NotNull
    private String reservationStatus;//TODO BOOKED, EXPIRED etc. ENUM TYPE

    @NotNull
    private boolean reservationIsApproved;


}
