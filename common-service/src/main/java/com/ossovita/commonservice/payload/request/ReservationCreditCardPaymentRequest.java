package com.ossovita.commonservice.payload.request;

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


    @NotBlank
    private String billingAddress;


}
