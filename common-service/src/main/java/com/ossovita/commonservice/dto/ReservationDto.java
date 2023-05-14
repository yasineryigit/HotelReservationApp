package com.ossovita.commonservice.dto;

import com.ossovita.commonservice.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {

    public long reservationPk;

    public long roomFk;

    public ReservationStatus reservationStatus;

    public double reservationPrice;

    private LocalDateTime reservationStartTime;

    private LocalDateTime reservationEndTime;




}
