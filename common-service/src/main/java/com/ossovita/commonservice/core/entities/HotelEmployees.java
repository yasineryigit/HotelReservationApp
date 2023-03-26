package com.ossovita.commonservice.core.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "restaurant_employees")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelEmployees {

    @Id
    @SequenceGenerator(name = "hotel_employee_seq", allocationSize = 1)
    @GeneratedValue(generator = "hotel_employee_seq")
    @Column(name = "employee_pk")
    private long restaurantEmployeePk;

    @ManyToOne
    @JoinColumn(name = "hotel_fk", insertable = false, updatable = false)
    @JsonIgnore
    private Hotel hotel;

    @Column(name = "hotel_fk")
    private long hotelFk;

    @ManyToOne
    @JoinColumn(name = "employee_fk", insertable = false, updatable = false)
    @JsonIgnore
    private Employee employee;

    @Column(name = "employee_fk")
    private long employeeFk;



}
