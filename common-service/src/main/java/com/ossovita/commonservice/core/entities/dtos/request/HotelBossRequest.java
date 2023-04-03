package com.ossovita.commonservice.core.entities.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelBossRequest {

    //hotel
    @NotNull
    private long hotelFk;

    //employee
    @NotNull
    private long bossFk;
}
