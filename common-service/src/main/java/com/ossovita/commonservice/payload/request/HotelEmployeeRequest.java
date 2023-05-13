package com.ossovita.commonservice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelEmployeeRequest {

    //hotel
    @NotNull
    private long hotelFk;

    //employee
    @NotNull
    private long employeeFk;
}
