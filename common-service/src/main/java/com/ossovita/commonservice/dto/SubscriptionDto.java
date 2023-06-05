package com.ossovita.commonservice.dto;

import com.ossovita.commonservice.enums.Currency;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubscriptionDto {

    private long subscriptionFk;

    private long bossFk;

    private BigDecimal subscriptionPrice;

    private Currency subscriptionPriceCurrency;
}
