package com.ossovita.userservice.controller;

import com.ossovita.userservice.payload.SubscriptionPlanDto;
import com.ossovita.userservice.service.SubscriptionPlanService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/1.0/user/subscription-plans")
public class SubscriptionPlanController {

    SubscriptionPlanService subscriptionPlanService;

    public SubscriptionPlanController(SubscriptionPlanService subscriptionPlanService) {
        this.subscriptionPlanService = subscriptionPlanService;
    }

    @PostMapping("/create-subscription-plan")
    SubscriptionPlanDto createSubscriptionPlan(@Valid @RequestBody SubscriptionPlanDto subscriptionPlanDto) {
        return subscriptionPlanService.createSubscriptionPlan(subscriptionPlanDto);
    }



}
