package com.ossovita.reservationservice.payload.response;

import com.ossovita.reservationservice.enums.ReservationStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
public class OnlineReservationResponse {


    private long reservationPk;

    private LocalDateTime reservationCreateTime;

    private LocalDateTime reservationStartTime;

    private LocalDateTime reservationEndTime;

    private double reservationPrice;

    private ReservationStatus reservationStatus;

    private boolean reservationIsApproved;

    private long roomFk;

    private long customerFk;

    private long onlineReservationFk;



}
