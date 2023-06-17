package com.ossovita.reservationservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ossovita.commonservice.enums.RoomStatus;
import com.ossovita.reservationservice.enums.ReservationCheckingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "reservation_checking")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationChecking {

    @Id
    @SequenceGenerator(name = "reservation_checking_seq", allocationSize = 1)
    @GeneratedValue(generator = "reservation_checking_seq")
    @Column(name = "reservation_checking_pk")
    private long reservationCheckingPk;

    @ManyToOne
    @JoinColumn(name = "reservation_fk", insertable = false, updatable = false)
    @JsonIgnore
    private Reservation reservation;

    @Column(name = "reservation_fk")
    private long reservationFk;

    @Column(name = "employee_fk")
    private long employeeFk;

    @Column(name = "reservation_checking_type")
    @Enumerated(EnumType.STRING)
    private ReservationCheckingType reservationCheckingType;

    @Column(name = "reservation_checking_time")
    private LocalDateTime reservationCheckingTime;


}
