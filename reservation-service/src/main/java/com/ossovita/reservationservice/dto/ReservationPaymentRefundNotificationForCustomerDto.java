package com.ossovita.reservationservice.dto;

import com.ossovita.commonservice.enums.ReservationPaymentRefundReason;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReservationPaymentRefundNotificationForCustomerDto {

    private long customerFk;

    private String customerEmail;

    private String customerFirstName;

    private String customerLastName;

    private long reservationPaymentPk;

    private ReservationPaymentRefundReason reservationPaymentRefundReason;

    private BigDecimal reservationPaymentAmount;

    private String reservationPaymentRefundMessage;
}
