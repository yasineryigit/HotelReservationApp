package com.ossovita.userservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "subscriptions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subscription {

    @Id
    @SequenceGenerator(name = "subscription_seq", allocationSize = 1)
    @GeneratedValue(generator = "subscription_seq")
    @Column(name = "subscription_pk")
    private long subscriptionPk;

    @ManyToOne
    @JoinColumn(name = "boss_fk", insertable = false, updatable = false)
    @JsonIgnore
    private Boss boss;

    @Column(name = "boss_fk")
    private long bossFk;

    @Column(name = "subscription_start_time")
    private LocalDateTime subscriptionStartTime;

    @Column(name = "subscription_end_time")
    private LocalDateTime subscriptionEndTime;

    @Column(name = "subscription_price")
    private BigDecimal subscriptionPrice;

    @Column(name = "is_paid")
    private boolean isPaid;

    @Column(name = "is_approved")
    private boolean isApproved;

    @Column(name = "is_active")
    private boolean isActive;


    @ManyToOne
    @JoinColumn(name = "subscription_plan_fk", insertable = false, updatable = false)
    private SubscriptionPlan subscriptionPlan;

    @Column(name = "subscription_plan_fk")
    private long subscriptionPlanFk;


}
