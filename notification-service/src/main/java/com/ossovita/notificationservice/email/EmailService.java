package com.ossovita.notificationservice.email;

import com.ossovita.commonservice.enums.NotificationType;

import java.util.HashMap;

public interface EmailService {

    void directEmail(String to, NotificationType notificationType, HashMap<String, String> payload);

    String loadEmailTemplate(String emailTemplatePath);

    void send(String to, String subject, String from, String text);

}
