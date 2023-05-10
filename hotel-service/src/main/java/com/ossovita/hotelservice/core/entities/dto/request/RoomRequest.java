package com.ossovita.hotelservice.core.entities.dto.request;

import javax.validation.constraints.*;

import com.ossovita.commonservice.core.enums.RoomStatus;
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
