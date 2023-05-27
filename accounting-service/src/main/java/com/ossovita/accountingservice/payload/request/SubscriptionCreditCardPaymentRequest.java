package com.ossovita.accountingservice.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SubscriptionCreditCardPaymentRequest {

    @NotNull
    private long bossFk;

    @NotNull
    private long subscriptionFk;

    @NotBlank
    private String cardHolder;

    @NotBlank
    private String cardNumber;

    @NotNull
    private int expirationMonth;

    @NotNull
    private int expirationYear;

    @NotNull
    private int cvv;

    @NotBlank
    private String billingAddress;


}
