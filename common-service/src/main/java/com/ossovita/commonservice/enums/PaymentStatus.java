package com.ossovita.commonservice.enums;

public enum PaymentStatus {
    PAID,
    PENDING,//to use in communication via payment provider
    FAILED,
    CANCELED
}