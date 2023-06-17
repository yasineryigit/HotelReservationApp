package com.ossovita.reservationservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ossovita.commonservice.enums.Currency;
import com.ossovita.commonservice.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    @Column(name = "reservation_price_currency")
    @Enumerated(EnumType.STRING)
    private Currency reservationPriceCurrency;

    @Column(name = "room_fk")
    private long roomFk;

    @Column(name = "customer_fk")
    private long customerFk;

    @OneToMany(mappedBy = "reservation")
    @JsonIgnore
    private List<ReservationChecking> reservationChecking;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "reservation")
    private OnlineReservation onlineReservation;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "reservation")
    private WalkInReservation walkInReservation;

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationPk=" + reservationPk +
                ", reservationCreateTime=" + reservationCreateTime +
                ", reservationStartTime=" + reservationStartTime +
                ", reservationEndTime=" + reservationEndTime +
                ", reservationPrice=" + reservationPrice +
                ", reservationStatus=" + reservationStatus +
                ", reservationIsApproved=" + reservationIsApproved +
                ", reservationPriceCurrency=" + reservationPriceCurrency +
                ", roomFk=" + roomFk +
                ", customerFk=" + customerFk +
                '}';
    }



}
