package com.ossovita.reservationservice.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "reviews")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {

    @Id
    @SequenceGenerator(name = "review_seq", allocationSize = 1)
    @GeneratedValue(generator = "review_seq")
    @Column(name = "review_pk")
    private long reviewPk;

    @Column(name = "review_text")
    private String reviewText;

    @Column(name = "review_point")
    private int reviewPoint;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "reservation_fk", insertable = false, updatable = false)
    private Reservation reservation;

    @Column(name = "reservation_fk")
    private long reservationFk;

    @ManyToOne
    @JoinColumn(name = "customer_fk", insertable = false, updatable = false)
    @JsonIgnore
    private Customer customer;

    @Column(name = "customer_fk")
    private long customerFk;


}
