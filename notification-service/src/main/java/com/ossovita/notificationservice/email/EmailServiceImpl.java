package com.ossovita.notificationservice.email;

import com.ossovita.commonservice.enums.NotificationType;
import com.ossovita.notificationservice.service.CustomerEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    CustomerEmailService customerEmailService;

    public EmailServiceImpl(CustomerEmailService customerEmailService) {
        this.customerEmailService = customerEmailService;
    }

    @Override
    public void send(String to, NotificationType notificationType, HashMap<String, String> payload) {
        switch (notificationType) {
            case CUSTOMER_WELCOME_NOTIFICATION:
                customerEmailService.sendCustomerWelcomeEmail(to, payload);
                break;
            case RESERVATION_BOOKED_NOTIFICATION:
                customerEmailService.sendReservationBookedEmailToTheCustomer(to, payload);
                //TODO: add each case
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


}
