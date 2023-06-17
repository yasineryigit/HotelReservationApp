package com.ossovita.reservationservice.dto;

import com.ossovita.commonservice.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationBookedNotificationForCustomerDto {

    private String userEmail;

    private String userFirstName;

    private String userLastName;

    private long reservationPk;

    private LocalDateTime reservationStartTime;

    private LocalDateTime reservationEndTime;

    private BigDecimal reservationPrice;

    private Currency reservationPriceCurrency;

    private String hotelName;

    private int roomNumber;


}
