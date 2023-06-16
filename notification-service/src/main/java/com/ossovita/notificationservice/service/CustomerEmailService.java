package com.ossovita.notificationservice.service;

public interface CustomerEmailService {

    void sendCustomerWelcomeEmail(String to, Object payload);

    void sendReservationBookedEmailToTheCustomer(String to, Object payload);

}
