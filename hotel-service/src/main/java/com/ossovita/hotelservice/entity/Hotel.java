package com.ossovita.hotelservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "hotels")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hotel {

    @Id
    @SequenceGenerator(name = "hotel_seq", allocationSize = 1)
    @GeneratedValue(generator = "hotel_seq")
    @Column(name = "hotel_pk")
    private long hotelPk;

    @Column(name = "hotel_name")
    private String hotelName;

    @Column(name = "hotel_phone")
    private String hotelPhone;

    @Column(name = "hotel_star")
    private int hotelStar;

    @OneToMany(mappedBy = "hotel")
    @JsonIgnore
    private List<Room> rooms;

    @OneToMany(mappedBy = "hotel")
    private List<HotelImage> hotelImages;

    @OneToMany(mappedBy = "hotel")
    @JsonIgnore
    private List<HotelEmployee> hotelEmployees;

    @OneToMany(mappedBy = "hotel")
    @JsonIgnore
    private List<HotelBoss> hotelBosses;


    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_fk", insertable = false, updatable = false)
    private Address address;

    @Column(name = "address_fk")
    private long addressFk;


}
