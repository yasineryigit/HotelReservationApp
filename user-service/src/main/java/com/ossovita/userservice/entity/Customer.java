package com.ossovita.userservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "customers")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {

    @Id
    @SequenceGenerator(name = "customer_seq", allocationSize = 1)
    @GeneratedValue(generator = "customer_seq")
    @Column(name = "customer_pk")
    private long customerPk;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_fk", insertable = false, updatable = false)
    private User user;

    @Column(name = "user_fk")
    private long userFk;

    @Column(name = "customer_stripe_id")
    private String customerStripeId;


}
