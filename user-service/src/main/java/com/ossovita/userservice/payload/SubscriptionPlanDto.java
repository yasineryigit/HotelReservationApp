package com.ossovita.userservice.payload;

import com.ossovita.commonservice.enums.Currency;
import lombok.Data;

@Data
public class SubscriptionPlanDto {

    private int subscriptionDayLength;

    private int subscriptionPrice;

    private Currency subscriptionPriceCurrency;

}
