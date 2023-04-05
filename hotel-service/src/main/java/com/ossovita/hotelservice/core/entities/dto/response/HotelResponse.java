package com.ossovita.hotelservice.core.entities.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ossovita.hotelservice.core.entities.*;
import lombok.Data;

import javax.persistence.*;
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
