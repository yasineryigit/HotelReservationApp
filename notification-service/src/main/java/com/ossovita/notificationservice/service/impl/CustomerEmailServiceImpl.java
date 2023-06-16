package com.ossovita.notificationservice.service.impl;

import com.ossovita.commonservice.dto.notification.CustomerWelcomeNotificationDto;
import com.ossovita.commonservice.dto.notification.ReservationBookedNotificationForCustomerDto;
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
    public void sendCustomerWelcomeEmail(String to, Object payload) {
        CustomerWelcomeNotificationDto dto = (CustomerWelcomeNotificationDto) payload;

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(buildCustomerWelcomeEmail(dto.getCustomerEmail()), true);
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
    public void sendReservationBookedEmailToTheCustomer(String to, Object payload) {
        ReservationBookedNotificationForCustomerDto dto = (ReservationBookedNotificationForCustomerDto) payload;

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(buildCustomerWelcomeEmail(dto.getUserEmail()), true);
            helper.setTo(to);
            helper.setSubject("Welcome to HRA");
            helper.setFrom("noreply@hra.com");
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

    private String buildReservationBookedEmailToTheCustomer(String userFirstName) {
        String emailTemplate = emailService.loadEmailTemplate(customerReservationBookedEmailTemplatePath);
        return emailTemplate.replace("{{userFirstName}}", userFirstName);
    }


}
