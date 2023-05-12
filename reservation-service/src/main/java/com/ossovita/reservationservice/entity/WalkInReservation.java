package com.ossovita.reservationservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "walk_in_reservations")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalkInReservation {

    @Id
    @SequenceGenerator(name = "walk_in_reservation_seq", allocationSize = 1)
    @GeneratedValue(generator = "walk_in_reservation_seq")
    @Column(name = "walk_in_reservation_pk")
    private long walkInReservationPk;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "reservation_fk", insertable = false, updatable = false)
    private Reservation reservation;

    @Column(name = "reservation_fk")
    private long reservationFk;

    @Column(name = "employee_fk")
    private long employeeFk;



}
