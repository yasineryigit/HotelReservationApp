package com.ossovita.hotelservice.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelBossResponse {


    //hotel
    private long hotelFk;

    //employee
    private long bossFk;
}

