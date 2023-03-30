package com.ossovita.accountingservice.core.entities;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "reservation_payment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationPayment {


    @Id
    @SequenceGenerator(name = "reservation_payment_seq", allocationSize = 1)
    @GeneratedValue(generator = "reservation_payment_seq")
    @Column(name = "reservation_payment_pk")
    private long reservationPaymentPk;

    @Column(name = "reservation_payment_status")
    private String reservationPaymentStatus;

    @Column(name = "reservation_payment_type")
    private String reservationPaymentType;

    @Column(name = "reservation_payment_amount")
    private int reservationPaymentAmount;

    @Column(name = "reservation_fk")
    private long reservationFk;


}
