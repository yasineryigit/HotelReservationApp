package com.ossovita.accountingservice.entity;

import com.ossovita.commonservice.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "subscription_payment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionPayment {


    @Id
    @SequenceGenerator(name = "subscription_payment_seq", allocationSize = 1)
    @GeneratedValue(generator = "subscription_payment_seq")
    @Column(name = "subscription_payment_pk")
    private long subscriptionPaymentPk;

    @Column(name = "boss_fk")
    private long bossFk;

    @Column(name = "subscription_fk")
    private long subscriptionFk;

    @Column(name = "subscription_payment_status")
    private PaymentStatus subscriptionPaymentStatus;

    @Column(name = "subscription_payment_amount")
    private BigDecimal subscriptionPaymentAmount;


}
