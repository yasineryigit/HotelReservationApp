package com.ossovita.notificationservice.email;

import com.ossovita.commonservice.enums.NotificationType;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {


    @Override
    public void send(String to, NotificationType notificationType) {
        switch (notificationType) {
            case CUSTOMER_WELCOME_NOTIFICATION -> sendCustomerWelcomeNotificationEmail(to);
            //TODO: add each case
            //TODO: add default mail template sender
        }
    }


    private void sendCustomerWelcomeNotificationEmail(String to) {



    }
}
