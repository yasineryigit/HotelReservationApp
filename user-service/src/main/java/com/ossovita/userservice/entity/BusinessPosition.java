package com.ossovita.userservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "business_positions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessPosition {

    @Id
    @SequenceGenerator(name = "business_position_seq", allocationSize = 1)
    @GeneratedValue(generator = "business_position_seq")
    @Column(name = "business_position_pk")
    private long businessPositionPk;

    @Column(name = "employee_position_name")
    private String businessPositionName;

    @OneToMany(mappedBy = "businessPosition")
    @JsonIgnore
    private List<Employee> employees;

    @OneToMany(mappedBy = "businessPosition")
    @JsonIgnore
    private List<Boss> bosses;


}

