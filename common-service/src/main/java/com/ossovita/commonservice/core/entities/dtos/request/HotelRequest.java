package com.ossovita.commonservice.core.entities.dtos.request;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class HotelRequest {

    //hotel
    @NotNull
    private String hotelName;

    @NotNull
    private double hotelStar;

    //address
    @NotNull
    private String addressCountry;

    @NotNull
    private String addressCity;

    @NotNull
    private String addressTown;

    @NotNull
    private String addressDetailed;


    @NotNull
    private long employeeFk;
}
