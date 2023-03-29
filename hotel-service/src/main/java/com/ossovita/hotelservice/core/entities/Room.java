package com.ossovita.hotelservice.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "rooms")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Room {


    @Id
    @SequenceGenerator(name = "room_seq", allocationSize = 1)
    @GeneratedValue(generator = "room_seq")
    @Column(name = "room_pk")
    private long roomPk;

    @Column(name = "room_type")
    private String roomType;

    @Column(name = "room_number")
    private int roomNumber;

    @Column(name = "room_price")
    private int roomPrice;

    @Column(name = "room_status")
    private String roomStatus;

    @ManyToOne
    @JoinColumn(name = "hotel_fk", insertable = false, updatable = false)
    @JsonIgnore
    private Hotel hotel;

    @Column(name = "hotel_fk")
    private long hotelFk;


}
