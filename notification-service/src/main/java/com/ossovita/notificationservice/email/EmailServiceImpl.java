package com.ossovita.notificationservice.email;

import com.ossovita.commonservice.enums.NotificationType;
import com.ossovita.notificationservice.service.CustomerEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    CustomerEmailService customerEmailService;

    public EmailServiceImpl(JavaMailSender mailSender, CustomerEmailService customerEmailService) {
        this.mailSender = mailSender;
        this.customerEmailService = customerEmailService;
    }

    @Override
    public void directEmail(String to, NotificationType notificationType, HashMap<String, String> payload) {
        switch (notificationType) {
            case CUSTOMER_WELCOME_NOTIFICATION:
                customerEmailService.sendCustomerWelcomeEmail(to, payload);
                break;
            case RESERVATION_BOOKED_NOTIFICATION:
                customerEmailService.sendReservationBookedEmailToTheCustomer(to, payload);
                break;
            case CHECK_IN_NOTIFICATION:
                customerEmailService.sendCheckInEmailToTheCustomer(to, payload);
                break;
            case CHECK_OUT_NOTIFICATION:
                customerEmailService.sendCheckOutEmailToTheCustomer(to, payload);
                break;
            case RESERVATION_PAYMENT_REFUND_NOTIFICATION:
                customerEmailService.sendReservationPaymentRefundEmailToTheCustomer(to, payload);
                break;
                //TODO: add default mail template sender
        }
    }

    @Override
    public String loadEmailTemplate(String emailTemplatePath) {
        try {
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(emailTemplatePath);
            byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Failed to load email template", e);
            throw new IllegalArgumentException("Failed to load email template");
        }
    }

    @Override
    public void send(String to, String subject, String from, String text) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(text, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(from);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email");
        }
    }


}
