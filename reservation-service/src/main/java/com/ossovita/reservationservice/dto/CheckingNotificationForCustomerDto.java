package com.ossovita.reservationservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CheckingNotificationForCustomerDto {

    private String customerEmail;

    private String customerFirstName;

    private String customerLastName;

    private LocalDateTime reservationStartTime;

    private LocalDateTime reservationEndTime;

    private String hotelName;

    private int roomNumber;


}
