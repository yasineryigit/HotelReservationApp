package com.ossovita.hotelservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ossovita.commonservice.enums.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private double roomPrice;

    @Column(name = "room_status")
    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatus;

    @ManyToOne
    @JoinColumn(name = "hotel_fk", insertable = false, updatable = false)
    @JsonIgnore
    private Hotel hotel;

    @Column(name = "hotel_fk")
    private long hotelFk;


}
