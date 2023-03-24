package com.ossovita.commonservice.core.entities.dtos;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RoomSaveFormDto {


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
    private String roomStatus;//TOOD enum type : CLEAN, DIRTY, INSPECTED, MAINTENANCE


}
