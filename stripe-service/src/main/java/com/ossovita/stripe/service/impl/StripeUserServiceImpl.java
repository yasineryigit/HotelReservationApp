package com.ossovita.stripe.service.impl;

import com.ossovita.stripe.service.StripeUserService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeUserServiceImpl implements StripeUserService {

    private final String stripeApiKey;

    public StripeUserServiceImpl(@Value("${stripe.api.key}") String stripeApiKey) {
        this.stripeApiKey = stripeApiKey;
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public Customer createCustomer(String customerFirstName, String customerLastName, String customerEmail) {
        CustomerCreateParams createParams = CustomerCreateParams.builder()
                .setName(customerFirstName + " " + customerLastName)
                .setEmail(customerEmail)
                .build();

        try {
            return Customer.create(createParams);
        } catch (StripeException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
