package com.ossovita.notificationservice.service;

import java.util.HashMap;

public interface CustomerEmailService {

    void sendCustomerWelcomeEmail(String to, HashMap<String, String> payload);

    void sendReservationBookedEmailToTheCustomer(String to, HashMap<String, String> payload);

    void sendCheckInEmailToTheCustomer(String to, HashMap<String, String> payload);

    void sendCheckOutEmailToTheCustomer(String to, HashMap<String, String> payload);

    void sendReservationPaymentRefundEmailToTheCustomer(String to, HashMap<String, String> payload);
}
