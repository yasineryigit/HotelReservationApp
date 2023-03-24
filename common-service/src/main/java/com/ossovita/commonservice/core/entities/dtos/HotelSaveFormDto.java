package com.ossovita.commonservice.core.entities.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HotelSaveFormDto {

    //hotel
    @NotNull
    private String hotelName;

    @NotNull
    private double hotelStar;

    @NotNull
    private String hotelPhone;

    //address
    @NotNull
    private String addressCountry;

    @NotNull
    private String addressCity;

    @NotNull
    private String addressTown;

    @NotNull
    private String addressDetailed;
}
