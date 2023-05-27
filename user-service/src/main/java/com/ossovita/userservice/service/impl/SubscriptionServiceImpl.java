package com.ossovita.userservice.service.impl;

import com.ossovita.commonservice.dto.SubscriptionDto;
import com.ossovita.commonservice.enums.PaymentStatus;
import com.ossovita.commonservice.exception.IdNotFoundException;
import com.ossovita.commonservice.exception.UnexpectedRequestException;
import com.ossovita.kafka.model.SubscriptionPaymentResponse;
import com.ossovita.userservice.entity.Subscription;
import com.ossovita.userservice.entity.SubscriptionPlan;
import com.ossovita.userservice.payload.request.SubscriptionRequest;
import com.ossovita.userservice.payload.response.SubscriptionResponse;
import com.ossovita.userservice.repository.SubscriptionRepository;
import com.ossovita.userservice.service.BossService;
import com.ossovita.userservice.service.SubscriptionPlanService;
import com.ossovita.userservice.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    BossService bossService;
    SubscriptionPlanService subscriptionPlanService;
    SubscriptionRepository subscriptionRepository;
    ModelMapper modelMapper;

    public SubscriptionServiceImpl(BossService bossService, SubscriptionPlanService subscriptionPlanService, SubscriptionRepository subscriptionRepository, ModelMapper modelMapper) {
        this.bossService = bossService;
        this.subscriptionPlanService = subscriptionPlanService;
        this.subscriptionRepository = subscriptionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public SubscriptionResponse createSubscription(SubscriptionRequest subscriptionRequest) {
        //check boss & subscriptionplanfk
        SubscriptionPlan subscriptionPlan = subscriptionPlanService.getSubscriptionPlan(subscriptionRequest.getSubscriptionPlanFk());
        if (bossService.isBossAvailable(subscriptionRequest.getBossFk())) {
            //create subscription via data in the subscription plan
            Subscription subscription = Subscription.builder()
                    .bossFk(subscriptionRequest.getBossFk())
                    .subscriptionPlanFk(subscriptionPlan.getSubscriptionPlanPk())
                    .subscriptionPrice(subscriptionPlan.getSubscriptionPrice())
                    .isApproved(false)
                    .isPaid(false)
                    .isActive(false)
                    .build();
            return modelMapper.map(subscriptionRepository.save(subscription), SubscriptionResponse.class);
        } else {
            throw new IdNotFoundException("Boss is not available by given id");
        }
    }

    @Override
    public SubscriptionResponse approveSubscription(long subscriptionFk) {
        Subscription subscription = getSubscription(subscriptionFk);

        if (subscription.isApproved()) {
            throw new UnexpectedRequestException("Subscription is already approved");
        }

        if (!subscription.isPaid()) {
            throw new UnexpectedRequestException("Subscription needs to be paid before getting approved");
        }

        subscription.setApproved(true);
        subscription.setActive(true);
        subscription.setSubscriptionStartTime(LocalDateTime.now());
        subscription.setSubscriptionEndTime(LocalDateTime.now().plusDays(subscription.getSubscriptionPlan().getSubscriptionDayLength()));

        return modelMapper.map(subscriptionRepository.save(subscription), SubscriptionResponse.class);
    }

    @Override
    public SubscriptionDto getSubscriptionDtoBySubscriptionFk(long subscriptionFk) {
        Subscription subscription = getSubscription(subscriptionFk);
        return modelMapper.map(subscription, SubscriptionDto.class);
    }

    @KafkaListener(
            topics = "subscription-payment-response-topic",
            groupId = "foo",
            containerFactory = "subscriptionPaymentResponseKafkaListenerContainerFactory"//we need to assign containerFactory
    )
    public void listenSubscriptionPaymentResponse(SubscriptionPaymentResponse subscriptionPaymentResponse) {
        log.info("Subscription Payment Updated | ReservationPaymentResponseModel: " + subscriptionPaymentResponse.toString());
        Subscription subscriptionInDB = getSubscription(subscriptionPaymentResponse.getSubscriptionFk());
        if(subscriptionPaymentResponse.getSubscriptionPaymentStatus().equals(PaymentStatus.PAID)){
            subscriptionInDB.setPaid(true);
            subscriptionRepository.save(subscriptionInDB);
        }
    }


    private Subscription getSubscription(long subscriptionFk) {
        return subscriptionRepository.findById(subscriptionFk).orElseThrow(() -> {
            throw new IdNotFoundException("Subscription not found by given id");
        });
    }
}
