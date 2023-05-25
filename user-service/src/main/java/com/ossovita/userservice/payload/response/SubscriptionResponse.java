package com.ossovita.userservice.payload.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SubscriptionResponse {


    private long bossFk;

    private long subscriptionPlanFk;

    private LocalDateTime subscriptionStartTime;

    private LocalDateTime subscriptionEndTime;

    private BigDecimal subscriptionPrice;

    private boolean isActive;


}
