package com.ossovita.accountingservice.entity;

import com.ossovita.commonservice.enums.ReservationPaymentType;
import com.ossovita.commonservice.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

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
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "reservation_payment_type")
    @Enumerated(EnumType.STRING)
    private ReservationPaymentType reservationPaymentType;

    @Column(name = "reservation_payment_amount")
    private BigDecimal reservationPaymentAmount;

    @Column(name = "reservation_fk")
    private long reservationFk;


}
