package com.ossovita.accountingservice.controller;

import com.ossovita.accountingservice.payload.request.SubscriptionCreditCardPaymentRequest;
import com.ossovita.accountingservice.service.SubscriptionPaymentService;
import com.ossovita.commonservice.enums.Currency;
import com.ossovita.kafka.model.SubscriptionPaymentResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/1.0/accounting/subscription-payments")
public class SubscriptionPaymentController {

    SubscriptionPaymentService subscriptionPaymentService;

    public SubscriptionPaymentController(SubscriptionPaymentService subscriptionPaymentService) {
        this.subscriptionPaymentService = subscriptionPaymentService;
    }

    @PostMapping("/create-subscription-payment")
    public SubscriptionPaymentResponse createSubscriptionPayment(@Valid @RequestBody SubscriptionCreditCardPaymentRequest subscriptionCreditCardPaymentRequest){
        return subscriptionPaymentService.createSubscriptionPayment(subscriptionCreditCardPaymentRequest);
    }





}
