package com.ossovita.hotelservice.payload.request;

import com.sun.istack.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AvailableRoomsByDateRangeAndCityRequest {

    @NotNull
    private LocalDateTime reservationStartTime;

    @NotNull
    private LocalDateTime reservationEndTime;

    @NotNull
    private String addressCity;
}
