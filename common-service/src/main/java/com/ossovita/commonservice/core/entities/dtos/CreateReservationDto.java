package com.ossovita.commonservice.core.entities.dtos;


import lombok.Data;

import javax.validation.constraints.NotNull;
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
