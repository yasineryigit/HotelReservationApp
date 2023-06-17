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
            helper.setText(buildReservationBookedEmailToTheCustomer(customerFirstName, reservationStartTime, reservationEndTime, reservationPrice, reservationPriceCurrency, hotelName, roomNumber), true);
            helper.setTo(to);
            helper.setSubject("Reservation Booked");
            helper.setFrom("booking@hra.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email");
        }
    }


    private String buildCustomerWelcomeEmail(String userFirstName) {
        String emailTemplate = emailService.loadEmailTemplate(customerWelcomeEmailTemplatePath);
        return emailTemplate.replace("{{userFirstName}}", userFirstName);
    }

    private String buildReservationBookedEmailToTheCustomer(String userFirstName,
                                                            String reservationStartTime,
                                                            String reservationEndTime,
                                                            String reservationPrice,
                                                            String reservationPriceCurrency,
                                                            String hotelName,
                                                            String roomNumber) {
        String emailTemplate = emailService.loadEmailTemplate(customerReservationBookedEmailTemplatePath);

        return emailTemplate.replace("{{userFirstName}}", userFirstName)
                .replace("{{hotelName}}", hotelName)
                .replace("{{roomNumber}}", roomNumber)
                .replace("{{reservationStartTime}}", reservationStartTime)
                .replace("{{reservationEndTime}}", reservationEndTime)
                .replace("{{reservationPrice}}", reservationPrice)
                .replace("{{reservationPriceCurrency}}", reservationPriceCurrency);
    }


}

