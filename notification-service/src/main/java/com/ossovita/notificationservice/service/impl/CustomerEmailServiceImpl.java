package com.ossovita.notificationservice.service.impl;

import com.ossovita.notificationservice.email.EmailService;
import com.ossovita.notificationservice.service.CustomerEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
public class CustomerEmailServiceImpl implements CustomerEmailService {

    @Value("${customer.welcome.email.template.path}")
    String customerWelcomeEmailTemplatePath;
    @Value("${customer.reservation.booked.email.template.path}")
    String customerReservationBookedEmailTemplatePath;
    @Value("${customer.check.in.email.template.path}")
    String customerCheckInEmailTemplatePath;
    @Value("${customer.check.out.email.template.path}")
    String customerCheckOutEmailTemplatePath;
    @Value("${customer.reservation.payment.refund.email.template.path}")
    String customerReservationPaymentRefundEmailTemplatePath;


    private EmailService emailService;


    public CustomerEmailServiceImpl(@Lazy EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void sendCustomerWelcomeEmail(String to, HashMap<String, String> payload) {
        String customerFirstName = payload.get("customer_first_name");
        String customerLastName = payload.get("customer_last_name");

        emailService.send(to,
                "Welcome to the HRA",
                "noreply@hra.com",
                buildCustomerWelcomeEmail(customerFirstName, customerLastName));
    }

    @Override
    public void sendReservationBookedEmailToTheCustomer(String to, HashMap<String, String> payload) {
        String customerEmail = payload.get("customer_email");
        String customerFirstName = payload.get("customer_first_name");
        String customerLastName = payload.get("customer_last_name");
        String reservationStartTime = payload.get("reservation_start_time");
        String reservationEndTime = payload.get("reservation_end_time");
        String reservationPrice = payload.get("reservation_price");
        String reservationPriceCurrency = payload.get("reservation_price_currency");
        String hotelName = payload.get("hotel_name");
        String roomNumber = payload.get("room_number");

        emailService.send(to,
                "Reservation Booked",
                "booking@hra.com",
                buildReservationBookedEmailToTheCustomer(customerFirstName, customerLastName, reservationStartTime, reservationEndTime, reservationPrice, reservationPriceCurrency, hotelName, roomNumber));
    }

    @Override
    public void sendCheckInEmailToTheCustomer(String to, HashMap<String, String> payload) {
        String customerEmail = payload.get("customer_email");
        String customerFirstName = payload.get("customer_first_name");
        String customerLastName = payload.get("customer_last_name");
        String reservationStartTime = payload.get("reservation_start_time");
        String reservationEndTime = payload.get("reservation_end_time");
        String hotelName = payload.get("hotel_name");
        String roomNumber = payload.get("room_number");

        emailService.send(to,
                "Check-In Confirmation",
                "booking@hra.com",
                buildCheckInEmailToTheCustomer(customerFirstName, customerLastName, reservationStartTime, reservationEndTime, hotelName, roomNumber));
    }

    @Override
    public void sendCheckOutEmailToTheCustomer(String to, HashMap<String, String> payload) {
        String customerEmail = payload.get("customer_email");
        String customerFirstName = payload.get("customer_first_name");
        String customerLastName = payload.get("customer_last_name");
        String reservationStartTime = payload.get("reservation_start_time");
        String reservationEndTime = payload.get("reservation_end_time");
        String hotelName = payload.get("hotel_name");
        String roomNumber = payload.get("room_number");

        emailService.send(to,
                "Check-Out Confirmation",
                "booking@hra.com",
                buildCheckOutEmailToTheCustomer(customerFirstName, customerLastName, reservationStartTime, reservationEndTime, hotelName, roomNumber));
    }

    @Override
    public void sendReservationPaymentRefundEmailToTheCustomer(String to, HashMap<String, String> payload) {
        String customerEmail = payload.get("customer_email");
        String customerFirstName = payload.get("customer_first_name");
        String customerLastName = payload.get("customer_last_name");
        String reservationPaymentRefundReason = payload.get("reservation_payment_refund_reason");
        String reservationPaymentRefundMessage = payload.get("reservation_payment_refund_message");
        String reservationPaymentAmount = payload.get("reservation_payment_amount");

        emailService.send(to,
                "Refund Confirmation",
                "accounting@hra.com",
                buildReservationPaymentRefundEmail(customerFirstName, customerLastName, reservationPaymentRefundReason, reservationPaymentRefundMessage, reservationPaymentAmount));

    }

    private String buildReservationPaymentRefundEmail(String customerFirstName,
                                                      String customerLastName,
                                                      String reservationPaymentRefundReason,
                                                      String reservationPaymentRefundMessage,
                                                      String reservationPaymentAmount) {

        String emailTemplate = emailService.loadEmailTemplate(customerReservationPaymentRefundEmailTemplatePath);

        return emailTemplate.replace("{{customerFirstName}}", customerFirstName)
                .replace("{{customerLastName}}", customerLastName)
                .replace("{{reservationPaymentReason}}", reservationPaymentRefundReason)
                .replace("{{reservationPaymentRefundMessage}}", reservationPaymentRefundMessage)
                .replace("{{reservationPaymentAmount}}", reservationPaymentAmount);


    }


    private String buildCustomerWelcomeEmail(String customerFirstName, String customerLastName) {
        String emailTemplate = emailService.loadEmailTemplate(customerWelcomeEmailTemplatePath);
        return emailTemplate.replace("{{customerFirstName}}", customerFirstName)
                .replace("{{customerLastName}}", customerLastName);
    }

    private String buildReservationBookedEmailToTheCustomer(String customerFirstName,
                                                            String customerLastName,
                                                            String reservationStartTime,
                                                            String reservationEndTime,
                                                            String reservationPrice,
                                                            String reservationPriceCurrency,
                                                            String hotelName,
                                                            String roomNumber) {
        String emailTemplate = emailService.loadEmailTemplate(customerReservationBookedEmailTemplatePath);

        return emailTemplate.replace("{{customerFirstName}}", customerFirstName)
                .replace("{{customerLastName}}", customerLastName)
                .replace("{{hotelName}}", hotelName)
                .replace("{{roomNumber}}", roomNumber)
                .replace("{{reservationStartTime}}", reservationStartTime)
                .replace("{{reservationEndTime}}", reservationEndTime)
                .replace("{{reservationPrice}}", reservationPrice)
                .replace("{{reservationPriceCurrency}}", reservationPriceCurrency);
    }

    private String buildCheckInEmailToTheCustomer(String customerFirstName,
                                                  String customerLastName,
                                                  String reservationStartTime,
                                                  String reservationEndTime,
                                                  String hotelName,
                                                  String roomNumber) {
        String emailTemplate = emailService.loadEmailTemplate(customerCheckInEmailTemplatePath);

        return emailTemplate.replace("{{customerFirstName}}", customerFirstName)
                .replace("{{customerLastName}}", customerLastName)
                .replace("{{hotelName}}", hotelName)
                .replace("{{roomNumber}}", roomNumber)
                .replace("{{reservationStartTime}}", reservationStartTime)
                .replace("{{reservationEndTime}}", reservationEndTime);
    }


    private String buildCheckOutEmailToTheCustomer(String customerFirstName,
                                                   String customerLastName,
                                                   String reservationStartTime,
                                                   String reservationEndTime,
                                                   String hotelName,
                                                   String roomNumber) {

        String emailTemplate = emailService.loadEmailTemplate(customerCheckOutEmailTemplatePath);
        return emailTemplate.replace("{{customerFirstName}}", customerFirstName)
                .replace("{{customerLastName}}", customerLastName)
                .replace("{{hotelName}}", hotelName)
                .replace("{{roomNumber}}", roomNumber)
                .replace("{{reservationStartTime}}", reservationStartTime)
                .replace("{{reservationEndTime}}", reservationEndTime);
    }


}

