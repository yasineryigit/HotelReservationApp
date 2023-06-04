package com.ossovita.stripe.service;

import com.ossovita.stripe.service.impl.StripePaymentServiceImpl;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;

import java.util.Map;

public interface StripePaymentService {

    PaymentIntent createPaymentIntent(PaymentIntentCreateParams paymentIntentCreateParams) throws StripeException;

    Refund createPaymentRefund(String paymentChargeId, Map<String, Object> params) throws StripeException;
}
