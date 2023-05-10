package com.ossovita.commonservice.core.enums;

public enum ReservationPaymentStatus {
    PAID,
    PENDING,//to use in communication via payment provider
    FAILED,
    CANCELED
}