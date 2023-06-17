package com.ossovita.notificationservice.service.impl;

import com.ossovita.notificationservice.email.EmailService;
import com.ossovita.notificationservice.service.CustomerEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
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

    private final JavaMailSender mailSender;
    private EmailService emailService;


    public CustomerEmailServiceImpl(JavaMailSender mailSender, @Lazy EmailService emailService) {
        this.mailSender = mailSender;
        this.emailService = emailService;
    }

    @Override
    public void sendCustomerWelcomeEmail(String to, HashMap<String, String> payload) {
        String customerFirstName = payload.get("customer_first_name");

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(buildCustomerWelcomeEmail(customerFirstName), true);
            helper.setTo(to);
            helper.setSubject("Welcome to HRA");
            helper.setFrom("noreply@hra.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email");
        }
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

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(buildReservationBookedEmailToTheCustomer(customerFirstName, customerLastName, reservationStartTime, reservationEndTime, reservationPrice, reservationPriceCurrency, hotelName, roomNumber), true);
            helper.setTo(to);
            helper.setSubject("Reservation Booked");
            helper.setFrom("booking@hra.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email");
        }
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

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(buildCheckInEmailToTheCustomer(customerFirstName, customerLastName, reservationStartTime, reservationEndTime, hotelName, roomNumber), true);
            helper.setTo(to);
            helper.setSubject("Check-In Confirmation");
            helper.setFrom("booking@hra.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email");
        }
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

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(buildCheckOutEmailToTheCustomer(customerFirstName, customerLastName, reservationStartTime, reservationEndTime, hotelName, roomNumber), true);
            helper.setTo(to);
            helper.setSubject("Check-Out Confirmation");
            helper.setFrom("booking@hra.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email");
        }
    }

    @Override
    public void sendReservationPaymentRefundEmailToTheCustomer(String to, HashMap<String, String> payload) {
        String customerEmail = payload.get("customer_email");
        String customerFirstName = payload.get("customer_first_name");
        String customerLastName = payload.get("customer_last_name");
        String reservationPaymentRefundReason = payload.get("reservation_payment_refund_reason");
        String reservationPaymentRefundMessage = payload.get("reservation_payment_refund_message");
        String reservationPaymentAmount = payload.get("reservation_payment_amount");

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(buildReservationPaymentRefundEmail(customerFirstName, customerLastName, reservationPaymentRefundReason, reservationPaymentRefundMessage, reservationPaymentAmount), true);
            helper.setTo(to);
            helper.setSubject("Check-Out Confirmation");
            helper.setFrom("booking@hra.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email");
        }

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


    private String buildCustomerWelcomeEmail(String userFirstName) {
        String emailTemplate = emailService.loadEmailTemplate(customerWelcomeEmailTemplatePath);
        return emailTemplate.replace("{{userFirstName}}", userFirstName);
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

