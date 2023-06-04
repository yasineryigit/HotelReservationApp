package com.ossovita.stripe.service;

import com.stripe.model.Customer;

public interface StripeUserService {

    Customer createCustomer(String customerFirstName, String customerLastName, String customerEmail);


}
