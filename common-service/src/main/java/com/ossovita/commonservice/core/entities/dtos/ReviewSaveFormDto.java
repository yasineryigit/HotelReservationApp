package com.ossovita.commonservice.core.entities.dtos;


import lombok.Data;

import javax.validation.constraints.NotNull;

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
