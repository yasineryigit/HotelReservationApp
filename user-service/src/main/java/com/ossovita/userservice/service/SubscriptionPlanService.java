package com.ossovita.userservice.service;

import com.ossovita.userservice.entity.SubscriptionPlan;
import com.ossovita.userservice.payload.SubscriptionPlanDto;

public interface SubscriptionPlanService {

    SubscriptionPlanDto createSubscriptionPlan(SubscriptionPlanDto subscriptionPlanDto);

    boolean isSubscriptionPlanAvailable(long subscriptionPlanPk);

    SubscriptionPlan getSubscriptionPlan(long subscriptionPlanPk);
}
