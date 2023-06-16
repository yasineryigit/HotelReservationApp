package com.ossovita.kafka.model;

import com.ossovita.commonservice.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequest {

    private String to;

    private NotificationType notificationType;

    Object payload;

}

