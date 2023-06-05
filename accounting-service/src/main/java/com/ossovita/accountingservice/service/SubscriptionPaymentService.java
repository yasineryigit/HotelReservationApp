package com.ossovita.accountingservice.service;


import com.ossovita.accountingservice.payload.request.SubscriptionPaymentRequest;
import com.ossovita.commonservice.payload.response.CreatePaymentResponse;
import com.ossovita.kafka.model.SubscriptionPaymentResponse;
import com.stripe.model.Charge;

public interface SubscriptionPaymentService {


    CreatePaymentResponse createSubscriptionPaymentIntent(SubscriptionPaymentRequest subscriptionPaymentRequest);

    void processSubscriptionPaymentCharge(Charge charge);
}
