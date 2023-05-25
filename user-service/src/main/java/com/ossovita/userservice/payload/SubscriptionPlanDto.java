package com.ossovita.userservice.payload;

import com.ossovita.commonservice.enums.Currency;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class SubscriptionPlanDto {

    @NotNull
    private int subscriptionDayLength;

    @NotNull
    private BigDecimal subscriptionPrice;

    @NotNull
    private Currency subscriptionPriceCurrency;

}
