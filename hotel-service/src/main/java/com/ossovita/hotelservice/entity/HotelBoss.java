package com.ossovita.hotelservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "hotel_bosses")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelBoss {

    @Id
    @SequenceGenerator(name = "hotel_boss_seq", allocationSize = 1)
    @GeneratedValue(generator = "hotel_boss_seq")
    @Column(name = "hotel_boss_pk")
    private long hotelBossPk;

    @ManyToOne
    @JoinColumn(name = "hotel_fk", insertable = false, updatable = false)
    @JsonIgnore
    private Hotel hotel;

    @Column(name = "hotel_fk")
    private long hotelFk;

    @Column(name = "boss_fk")
    private long bossFk;


}
