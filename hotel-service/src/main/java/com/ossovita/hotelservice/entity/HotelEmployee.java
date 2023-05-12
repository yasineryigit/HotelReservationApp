package com.ossovita.hotelservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "hotel_employees")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelEmployee {

    @Id
    @SequenceGenerator(name = "hotel_employee_seq", allocationSize = 1)
    @GeneratedValue(generator = "hotel_employee_seq")
    @Column(name = "hotel_employee_pk")
    private long hotelEmployeePk;

    @ManyToOne
    @JoinColumn(name = "hotel_fk", insertable = false, updatable = false)
    @JsonIgnore
    private Hotel hotel;

    @Column(name = "hotel_fk")
    private long hotelFk;

    @Column(name = "employee_fk")
    private long employeeFk;


}
