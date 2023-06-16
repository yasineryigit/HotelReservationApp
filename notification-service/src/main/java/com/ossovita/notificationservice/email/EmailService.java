package com.ossovita.notificationservice.email;

import com.ossovita.commonservice.enums.NotificationType;

public interface EmailService {

    void send(String to, NotificationType notificationType, Object payload);

    String loadEmailTemplate(String emailTemplatePath);

}
