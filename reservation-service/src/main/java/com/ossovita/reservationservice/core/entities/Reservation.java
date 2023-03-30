package com.ossovita.reservationservice.core.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "reservations")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation {

    @Id
    @SequenceGenerator(name = "reservation_seq", allocationSize = 1)
    @GeneratedValue(generator = "reservation_seq")
    @Column(name = "reservation_pk")
    private long reservationPk;

    @Column(name = "reservation_time")
    private LocalDateTime reservationTime;

    @Column(name = "reservation_price")
    private int reservationPrice;

    @Column(name = "reservation_status")
    private String reservationStatus;//TODO BOOKED, EXPIRED etc. ENUM TYPE

    @Column(name = "reservation_is_approved")
    private boolean reservationIsApproved;

    @Column(name = "room_fk")
    private long roomFk;

    @Column(name = "employee_fk")
    private long employeeFk;

    @Column(name = "customer_fk")
    private long customerFk;


}
