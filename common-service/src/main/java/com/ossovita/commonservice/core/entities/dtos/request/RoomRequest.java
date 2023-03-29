package com.ossovita.commonservice.core.entities.dtos.request;

import javax.validation.constraints.*;
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
    private int roomPrice;

    @NotNull
    private String roomStatus;


}
