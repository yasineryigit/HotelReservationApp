package com.ossovita.commonservice.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "tables")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {

    @Id
    @SequenceGenerator(name = "payment_seq", allocationSize = 1)
    @GeneratedValue(generator = "payment_seq")
    @Column(name = "payment_pk")
    private long paymentPk;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "payment_amount")
    private int paymentAmount;

    @ManyToOne
    @JoinColumn(name = "customer_fk", insertable = false, updatable = false)
    @JsonIgnore
    private Customer customer;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "reservation_fk", insertable = false, updatable = false)
    private Reservation reservation;

    @Column(name = "reservation_fk")
    private long reservationFk;


}
