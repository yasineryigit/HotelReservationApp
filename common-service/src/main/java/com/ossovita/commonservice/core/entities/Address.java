package com.ossovita.commonservice.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "address")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

    @Id
    @SequenceGenerator(name = "address_seq", allocationSize = 1)
    @GeneratedValue(generator = "address_seq")
    @Column(name = "address_pk")
    private long addressPk;

    private String addressCountry;

    private String addressCity;

    private String addressTown;

    private String addressDetailed;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "address")
    @JsonIgnore
    private Hotel hotel;





}
