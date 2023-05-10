package com.ossovita.commonservice.core.dto;

import com.ossovita.commonservice.core.enums.RoomStatus;
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
