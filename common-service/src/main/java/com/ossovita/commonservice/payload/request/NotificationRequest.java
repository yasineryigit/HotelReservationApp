package com.ossovita.commonservice.payload.request;

import com.ossovita.commonservice.enums.NotificationType;
import lombok.Data;

@Data
public class NotificationRequest {

    private String to;

    private NotificationType notificationType;



}
