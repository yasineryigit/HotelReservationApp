package com.ossovita.commonservice.payload.request;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class HotelRequest {

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


    @NotNull
    private long bossFk;
}
