package com.ossovita.userservice.service;

import com.ossovita.commonservice.dto.SubscriptionDto;
import com.ossovita.userservice.entity.Subscription;
import com.ossovita.userservice.payload.request.SubscriptionRequest;
import com.ossovita.userservice.payload.response.SubscriptionResponse;

public interface SubscriptionService {


    SubscriptionResponse createSubscription(SubscriptionRequest subscriptionDto);

    SubscriptionResponse approveSubscription(long subscriptionFk);

    SubscriptionDto getSubscriptionDtoBySubscriptionPk(long subscriptionFk);

    Subscription getSubscription(long subscriptionFk);

    Subscription save(Subscription subscriptionInDB);
}
