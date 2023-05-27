package com.ossovita.kafka.model;

import com.ossovita.commonservice.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionPaymentResponse {


    private long subscriptionPaymentPk;

    private long subscriptionFk;

    private long bossFk;

    private BigDecimal subscriptionPrice;

    private PaymentStatus subscriptionPaymentStatus;


}
