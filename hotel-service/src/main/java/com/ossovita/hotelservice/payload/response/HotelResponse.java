package com.ossovita.hotelservice.payload.response;

import lombok.Data;

import java.util.List;

//TODO
@Data
public class HotelResponse {

    //hotel
    private long hotelPk;

    private String hotelName;

    private String hotelPhone;

    private int hotelStar;

    //hotelImage
    private List<String> hotelImageUrls;

    //address
    private long addressPk;

    private String addressCountry;

    private String addressCity;

    private String addressTown;

    private String addressDetailed;







}
