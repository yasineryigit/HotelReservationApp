package com.ossovita.commonservice.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<Payment> payments;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<Review> reviews;






}
