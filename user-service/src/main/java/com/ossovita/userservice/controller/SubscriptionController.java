package com.ossovita.userservice.controller;

import com.ossovita.commonservice.dto.SubscriptionDto;
import com.ossovita.userservice.payload.request.SubscriptionRequest;
import com.ossovita.userservice.payload.response.SubscriptionResponse;
import com.ossovita.userservice.service.SubscriptionService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/1.0/user/subscriptions")
public class SubscriptionController {

    SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/create-subscription")
    public SubscriptionResponse createSubscription(@Valid @RequestBody SubscriptionRequest subscriptionRequest) {
        return subscriptionService.createSubscription(subscriptionRequest);
    }

    @PutMapping("/approve-subscription")
    public SubscriptionResponse updateSubscriptionApproval(@RequestParam long subscriptionFk) {
        return subscriptionService.approveSubscription(subscriptionFk);
    }

    @GetMapping("/get-subscription-dto-by-subscription-fk")
    SubscriptionDto getSubscriptionDtoBySubscriptionFk(@RequestParam long subscriptionFk){
        return subscriptionService.getSubscriptionDtoBySubscriptionFk(subscriptionFk);
    }


}
