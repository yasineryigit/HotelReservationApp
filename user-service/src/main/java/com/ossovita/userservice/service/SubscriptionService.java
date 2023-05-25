package com.ossovita.userservice.service;

import com.ossovita.userservice.payload.request.SubscriptionRequest;
import com.ossovita.userservice.payload.response.SubscriptionResponse;

public interface SubscriptionService {


    SubscriptionResponse createSubscription(SubscriptionRequest subscriptionDto);

    SubscriptionResponse approveSubscription(long subscriptionFk);
}
