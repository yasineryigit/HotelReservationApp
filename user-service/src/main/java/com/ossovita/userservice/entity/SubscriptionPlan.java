package com.ossovita.userservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ossovita.commonservice.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table(name = "subscription_plans")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionPlan {

    @Id
    @SequenceGenerator(name = "subscription_plan_seq", allocationSize = 1)
    @GeneratedValue(generator = "subscription_plan_seq")
    @Column(name = "subscription_plan_pk")
    private long subscriptionPlanPk;

    @Column(name = "subscription_plan_day_length")
    private int subscriptionPlanDayLength;

    @Column(name = "subscription_plan_price")
    private BigDecimal subscriptionPlanPrice;

    @Column(name = "subscription_plan_price_currency")
    @Enumerated(EnumType.STRING)
    private Currency subscriptionPlanPriceCurrency;

    @OneToMany(mappedBy = "subscriptionPlan")
    @JsonIgnore
    private List<Subscription> subscriptionList;



}
