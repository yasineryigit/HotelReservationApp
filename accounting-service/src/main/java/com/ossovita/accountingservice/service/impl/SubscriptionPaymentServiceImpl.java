package com.ossovita.accountingservice.service.impl;

import com.ossovita.accountingservice.entity.SubscriptionPayment;
import com.ossovita.accountingservice.enums.PaymentType;
import com.ossovita.accountingservice.payload.request.SubscriptionPaymentRequest;
import com.ossovita.accountingservice.repository.SubscriptionPaymentRepository;
import com.ossovita.accountingservice.service.SubscriptionPaymentService;
import com.ossovita.clients.user.UserClient;
import com.ossovita.commonservice.dto.BossDto;
import com.ossovita.commonservice.dto.SubscriptionDto;
import com.ossovita.commonservice.enums.PaymentStatus;
import com.ossovita.commonservice.exception.IdNotFoundException;
import com.ossovita.commonservice.exception.UnexpectedRequestException;
import com.ossovita.accountingservice.payload.response.CreatePaymentResponse;
import com.ossovita.kafka.model.SubscriptionPaymentResponse;
import com.ossovita.stripe.service.StripePaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@Slf4j
public class SubscriptionPaymentServiceImpl implements SubscriptionPaymentService {

    StripePaymentService stripePaymentService;
    SubscriptionPaymentRepository subscriptionPaymentRepository;
    UserClient userClient;
    KafkaTemplate<String, Object> kafkaTemplate;

    public SubscriptionPaymentServiceImpl(StripePaymentService stripePaymentService, SubscriptionPaymentRepository subscriptionPaymentRepository, UserClient userClient, KafkaTemplate<String, Object> kafkaTemplate) {
        this.stripePaymentService = stripePaymentService;
        this.subscriptionPaymentRepository = subscriptionPaymentRepository;
        this.userClient = userClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public CreatePaymentResponse createSubscriptionPaymentIntent(SubscriptionPaymentRequest subscriptionPaymentRequest) {
        //check subscriptionFk -> price required | getSubscriptionDtoBySubscriptionFk
        //check bossFk -> isBossAvailable
        //check subscriptionDto.bossFk equals subscriptionCreditCardPaymentRequest.getBossFk() to prevent payment conflicts
        SubscriptionDto subscriptionDto = userClient.getSubscriptionDtoBySubscriptionFk(subscriptionPaymentRequest.getSubscriptionFk());

        if (subscriptionDto == null) {
            throw new IdNotFoundException("Subscription not found by given id");
        }
        BossDto bossDto = userClient.getBossDtoByBossPk(subscriptionPaymentRequest.getBossFk());

        if (bossDto == null) {
            throw new IdNotFoundException("Boss not found by given id");
        }

        if (subscriptionDto.getBossFk() != subscriptionPaymentRequest.getBossFk()) {
            throw new UnexpectedRequestException("Subscription id has a different id than the id you provided");
        }

        SubscriptionPayment subscriptionPayment = SubscriptionPayment.builder()
                .subscriptionFk(subscriptionPaymentRequest.getSubscriptionFk())
                .bossFk(subscriptionPaymentRequest.getBossFk())
                .subscriptionPaymentAmount(subscriptionDto.getSubscriptionPrice())
                .subscriptionPaymentStatus(PaymentStatus.PAID)
                .build();
        SubscriptionPayment savedSubscriptionPayment = subscriptionPaymentRepository.save(subscriptionPayment);

        //put extra information to the metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("payment_type", String.valueOf(PaymentType.SUBSCRIPTION_PAYMENT));
        metadata.put("boss_email", bossDto.getBossEmail());
        metadata.put("subscription_fk", String.valueOf(subscriptionPaymentRequest.getSubscriptionFk()));
        metadata.put("boss_fk", String.valueOf(bossDto.getBossPk()));
        metadata.put("subscription_payment_fk", String.valueOf(savedSubscriptionPayment.getSubscriptionPaymentPk()));

        PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
                .setCustomer(bossDto.getBossStripeId())
                .setCurrency(subscriptionDto.getSubscriptionPriceCurrency().toString().toLowerCase(Locale.ENGLISH))
                .setAmount(savedSubscriptionPayment.getSubscriptionPaymentAmount().longValue() * 100L)
                .putAllMetadata(metadata)
                .build();


        //Create a PaymentIntent with params
        PaymentIntent intent;
        try {
            intent = stripePaymentService.createPaymentIntent(createParams);
            return new CreatePaymentResponse(intent.getClientSecret());
        } catch (StripeException e) {//if fails, reflect to the database
            SubscriptionPayment subscriptionPaymentInDB = getSubscriptionPayment(savedSubscriptionPayment.getSubscriptionFk());//TODO: optimize
            subscriptionPaymentInDB.setSubscriptionPaymentStatus(PaymentStatus.FAILED);
            subscriptionPaymentRepository.save(subscriptionPaymentInDB);
            throw new RuntimeException(e);
        }


    }

    @Override
    public void processSubscriptionPaymentCharge(Charge charge) {
        Map<String, String> metadata = charge.getMetadata();
        long subscriptionFk = Long.parseLong(metadata.get("subscription_fk"));
        long bossFk = Long.parseLong(metadata.get("boss_fk"));
        long subscriptionPaymentFk = Long.parseLong(metadata.get("subscription_payment_fk"));

        log.info("processSubscriptionPaymentCharge: " + "subscriptionFk: " + subscriptionFk + " bossFk: " + bossFk + " subscriptionPaymentFk: " + subscriptionPaymentFk);


        SubscriptionPayment subscriptionPaymentInDB = getSubscriptionPayment(subscriptionFk);
        subscriptionPaymentInDB.setSubscriptionPaymentStripeChargeId(charge.getId());
        subscriptionPaymentInDB.setSubscriptionPaymentStatus(PaymentStatus.PAID);
        subscriptionPaymentRepository.save(subscriptionPaymentInDB);

        //update subscription object in the reservation-service with an event
        SubscriptionPaymentResponse subscriptionPaymentResponse = SubscriptionPaymentResponse
                .builder()
                .subscriptionFk(subscriptionPaymentInDB.getSubscriptionFk())
                .subscriptionPaymentPk(subscriptionPaymentInDB.getSubscriptionPaymentPk())
                .bossFk(subscriptionPaymentInDB.getBossFk())
                .subscriptionPrice(subscriptionPaymentInDB.getSubscriptionPaymentAmount())
                .subscriptionPaymentStatus(subscriptionPaymentInDB.getSubscriptionPaymentStatus())
                .build();

        //publish kafka event to approve is paid status in user-service
        kafkaTemplate.send("subscription-payment-response-topic", subscriptionPaymentResponse);
        log.info("Payment Update Response sent | SubscriptionPaymentResponse: " + subscriptionPaymentResponse.toString());


    }

    public SubscriptionPayment getSubscriptionPayment(long subscriptionPaymentPk) {
        return subscriptionPaymentRepository.findById(subscriptionPaymentPk).orElseThrow(() -> {
            throw new IdNotFoundException("Subscription not found by given id");
        });
    }
}
