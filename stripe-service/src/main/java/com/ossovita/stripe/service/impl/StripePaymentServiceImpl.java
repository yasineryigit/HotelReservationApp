package com.ossovita.stripe.service.impl;

import com.ossovita.stripe.service.StripePaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StripePaymentServiceImpl implements StripePaymentService {


    @Override
    public PaymentIntent createPaymentIntent(PaymentIntentCreateParams paymentIntentCreateParams) throws StripeException {
        return PaymentIntent.create(paymentIntentCreateParams);
    }

    @Override
    public Refund createPaymentRefund(String paymentChargeId, Map<String, Object> params) throws StripeException {
        Charge charge = Charge.retrieve(paymentChargeId);
        //send instructions email about refund to the user
        params.put("instructions_email", charge.getCustomerObject().getEmail());
        return Refund.create(params);

    }
}
