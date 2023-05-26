package com.ossovita.reservationservice.entity;

import com.ossovita.commonservice.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
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

    @Column(name = "reservation_create_time")
    private LocalDateTime reservationCreateTime;

    @Column(name = "reservation_start_time")
    private LocalDateTime reservationStartTime;

    @Column(name = "reservation_end_time")
    private LocalDateTime reservationEndTime;

    @Column(name = "reservation_price")
    private BigDecimal reservationPrice;

    @Column(name = "reservation_status")
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @Column(name = "reservation_is_approved")
    private boolean reservationIsApproved;

    @Column(name = "room_fk")
    private long roomFk;

    @Column(name = "customer_fk")
    private long customerFk;


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "reservation")
    private OnlineReservation onlineReservation;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "reservation")
    private WalkInReservation walkInReservation;





}
