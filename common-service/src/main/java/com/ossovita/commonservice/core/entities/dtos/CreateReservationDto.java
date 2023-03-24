package com.ossovita.commonservice.core.entities.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateReservationDto {

    @NotNull
    private long roomFk;

    @NotNull
    private long employeeFk;

    @NotNull
    private LocalDateTime reservationTime;

    @NotNull
    private int reservationPrice;

    @NotNull
    private String reservationStatus;//TODO BOOKED, EXPIRED etc. ENUM TYPE

    @NotNull
    private boolean reservationIsPaid;


}
