package com.ossovita.accountingservice.service;


import com.ossovita.accountingservice.payload.request.SubscriptionCreditCardPaymentRequest;
import com.ossovita.kafka.model.SubscriptionPaymentResponse;

public interface SubscriptionPaymentService {


    SubscriptionPaymentResponse createSubscriptionPayment(SubscriptionCreditCardPaymentRequest subscriptionCreditCardPaymentRequest);
}
