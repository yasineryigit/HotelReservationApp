package com.ossovita.userservice.core.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "employee_positions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeePosition {

    @Id
    @SequenceGenerator(name = "employee_position_seq", allocationSize = 1)
    @GeneratedValue(generator = "employee_position_seq")
    @Column(name = "employee_position_pk")
    private long employeePositionPk;

    @Column(name = "employee_position_name")
    private String employeePositionName;



}

