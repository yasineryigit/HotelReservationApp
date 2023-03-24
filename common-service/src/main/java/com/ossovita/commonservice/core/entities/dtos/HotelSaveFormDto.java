package com.ossovita.commonservice.core.entities.dtos;


import lombok.Data;

import javax.validation.constraints.NotNull;

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
