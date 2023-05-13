package com.ossovita.commonservice.dto;

import com.ossovita.commonservice.enums.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {

    private long roomPk;

    private double roomPrice;

    private RoomStatus roomStatus;


}
