package com.ossovita.accountingservice.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SubscriptionPaymentRequest {

    @NotNull
    private long bossFk;

    @NotNull
    private long subscriptionFk;



}
