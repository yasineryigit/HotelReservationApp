package com.ossovita.userservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private int subscriptionPrice;

    @Column(name = "is_active")
    private boolean isActive;


}
