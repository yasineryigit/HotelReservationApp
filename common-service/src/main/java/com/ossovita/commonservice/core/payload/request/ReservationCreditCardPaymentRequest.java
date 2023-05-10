package com.ossovita.commonservice.core.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ReservationCreditCardPaymentRequest {


    @NotNull
    private long reservationFk;

    @NotBlank
    private String cardHolder;

    @NotBlank
    private String cardNumber;

    @NotNull
    private int expirationMonth;

    @NotNull
    private int expirationYear;

    @NotBlank
    private String cvv;

    private String cardType;

    @NotBlank
    private String billingAddress;


}
