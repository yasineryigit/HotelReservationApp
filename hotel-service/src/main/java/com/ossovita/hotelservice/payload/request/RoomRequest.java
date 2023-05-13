package com.ossovita.hotelservice.payload.request;

import javax.validation.constraints.*;

import com.ossovita.commonservice.enums.RoomStatus;
import lombok.Data;

@Data
public class RoomRequest {


    //hotel
    private long hotelFk;

    //room
    @NotNull
    private String roomType;

    @NotNull
    private int roomNumber;

    @NotNull
    private double roomPrice;

    @NotNull
    private RoomStatus roomStatus;


}
