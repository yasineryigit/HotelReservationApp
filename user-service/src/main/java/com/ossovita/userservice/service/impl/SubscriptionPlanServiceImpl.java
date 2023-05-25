package com.ossovita.userservice.service.impl;

import com.ossovita.commonservice.exception.IdNotFoundException;
import com.ossovita.userservice.entity.SubscriptionPlan;
import com.ossovita.userservice.payload.SubscriptionPlanDto;
import com.ossovita.userservice.repository.SubscriptionPlanRepository;
import com.ossovita.userservice.service.SubscriptionPlanService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    ModelMapper modelMapper;
    SubscriptionPlanRepository subscriptionPlanRepository;

    public SubscriptionPlanServiceImpl(ModelMapper modelMapper, SubscriptionPlanRepository subscriptionPlanRepository) {
        this.modelMapper = modelMapper;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    @Override
    public SubscriptionPlanDto createSubscriptionPlan(SubscriptionPlanDto subscriptionPlanDto) {
        SubscriptionPlan savedSubscriptionPlan = subscriptionPlanRepository.save(modelMapper.map(subscriptionPlanDto, SubscriptionPlan.class));
        return modelMapper.map(savedSubscriptionPlan, SubscriptionPlanDto.class);
    }

    @Override
    public boolean isSubscriptionPlanAvailable(long subscriptionPlanPk) {
        return subscriptionPlanRepository.existsBySubscriptionPlanPk(subscriptionPlanPk);
    }

    @Override
    public SubscriptionPlan getSubscriptionPlan(long subscriptionPlanPk){
        return subscriptionPlanRepository.findById(subscriptionPlanPk).orElseThrow(()->{
            throw new IdNotFoundException("Subscription plan not found by given id");
        });
    }
}
