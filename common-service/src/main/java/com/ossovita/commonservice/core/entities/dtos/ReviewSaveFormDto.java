package com.ossovita.commonservice.core.entities.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewSaveFormDto {

    @NotNull
    private long customerFk;

    @NotNull
    private long reservationFk;

    @NotNull
    private String reviewText;

    @NotNull
    private double reviewPoint;



}
