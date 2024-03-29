package com.ossovita.commonservice.dto;

import com.ossovita.commonservice.enums.Currency;
import com.ossovita.commonservice.enums.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {

    private long roomPk;

    private BigDecimal roomPrice;

    private int roomNumber;

    private RoomStatus roomStatus;

    private Currency roomPriceCurrency;

    private String hotelName;


}
