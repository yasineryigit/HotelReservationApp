package com.ossovita.commonservice.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerWelcomeNotificationDto {

    private String customerEmail;
    private String customerFirstName;
    private String customerLastName;
}
