package com.ossovita.commonservice.core.entities.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

//TODO move to the hotel-service
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelEmployeeResponse {



    //hotel
    private long hotelFk;

    //employee
    private long employeeFk;
}
