package com.ossovita.commonservice.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.*;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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

    @Column(name = "reservation_is_paid")
    private boolean reservationIsPaid;

    @Column(name = "reservation_is_approved")
    private boolean reservationIsApproved;

    @ManyToOne
    @JoinColumn(name = "room_fk", insertable = false, updatable = false)
    private Room room;

    @Column(name = "room_fk")
    @NotNull
    private long roomFk;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "reservation")
    @JsonIgnore
    private Review review;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "reservation")
    @JsonIgnore
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "employee_fk", insertable = false, updatable = false)
    @JsonIgnore
    private Employee employee;

    @Column(name = "employee_fk")
    private long employeeFk;



}
