package com.ossovita.userservice.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "employees")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {

    @Id
    @SequenceGenerator(name = "employee_seq", allocationSize = 1)
    @GeneratedValue(generator = "employee_seq")
    @Column(name = "employee_pk")
    private long employeePk;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_fk", insertable = false, updatable = false)
    private User user;

    @Column(name = "user_fk")
    private long userFk;


    @ManyToOne
    @JoinColumn(name = "business_position_fk", insertable = false, updatable = false)
    private BusinessPosition businessPosition;

    @Column(name = "business_position_fk")
    private long businessPositionFk;

    @Column(name = "is_approved")
    private boolean isApproved;


}