package com.ossovita.hotelservice.payload.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class HotelWithImagesRequest {


    //hotel
    @NotNull
    private String hotelName;

    @NotNull
    private double hotelStar;

    //hotelImages
    private MultipartFile[] hotelImages;

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
