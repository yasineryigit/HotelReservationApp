package com.ossovita.hotelservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "hotel_images")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelImage {

    @Id
    @SequenceGenerator(name = "hotel_image_seq", allocationSize = 1)
    @GeneratedValue(generator = "hotel_image_seq")
    @Column(name = "hotel_image_pk")
    private long hotelImagePk;

    //hotel
    @ManyToOne
    @JoinColumn(name = "hotel_fk", insertable = false, updatable = false)
    @JsonIgnore
    private Hotel hotel;

    @Column(name = "hotel_fk")
    private long hotelFk;

    @Column(name = "hotel_image_url")
    private String hotelImageUrl;

    @Column(name = "hotel_image_name")
    private String hotelImageName;




}
