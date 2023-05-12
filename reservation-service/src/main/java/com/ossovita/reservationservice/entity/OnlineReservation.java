package com.ossovita.reservationservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "online_reservations")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OnlineReservation {

    @Id
    @SequenceGenerator(name = "online_reservation_seq", allocationSize = 1)
    @GeneratedValue(generator = "online_reservation_seq")
    @Column(name = "online_reservation_pk")
    private long onlineReservationPk;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "reservation_fk", insertable = false, updatable = false)
    private Reservation reservation;

    @Column(name = "reservation_fk")
    private long reservationFk;




}
