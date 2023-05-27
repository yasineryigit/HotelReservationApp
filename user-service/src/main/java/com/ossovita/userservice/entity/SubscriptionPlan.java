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

    @Column(name = "subscription_day_length")
    private int subscriptionDayLength;

    @Column(name = "subscription_price")
    private BigDecimal subscriptionPrice;

    @Column(name = "subscription_price_currency")
    @Enumerated(EnumType.STRING)
    private Currency subscriptionPriceCurrency;

    @OneToMany(mappedBy = "subscriptionPlan")
    @JsonIgnore
    private List<Subscription> subscriptionList;



}
