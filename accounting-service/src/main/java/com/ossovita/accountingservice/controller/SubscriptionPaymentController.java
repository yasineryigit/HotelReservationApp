package com.ossovita.accountingservice.controller;

import com.ossovita.accountingservice.payload.request.SubscriptionPaymentRequest;
import com.ossovita.accountingservice.service.SubscriptionPaymentService;
import com.ossovita.accountingservice.payload.response.CreatePaymentResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/1.0/accounting/subscription-payments")
public class SubscriptionPaymentController {

    SubscriptionPaymentService subscriptionPaymentService;

    public SubscriptionPaymentController(SubscriptionPaymentService subscriptionPaymentService) {
        this.subscriptionPaymentService = subscriptionPaymentService;
    }

    @PostMapping("/create-subscription-payment-intent")
    public CreatePaymentResponse createSubscriptionPaymentIntent(@Valid @RequestBody SubscriptionPaymentRequest subscriptionPaymentRequest){
        return subscriptionPaymentService.createSubscriptionPaymentIntent(subscriptionPaymentRequest);
    }





}
