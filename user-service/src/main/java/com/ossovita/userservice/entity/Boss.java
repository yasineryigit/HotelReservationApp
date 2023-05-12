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
@Table(name = "bosses")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Boss {


    @Id
    @SequenceGenerator(name = "boss_seq", allocationSize = 1)
    @GeneratedValue(generator = "boss_seq")
    @Column(name = "boss_pk")
    private long bossPk;

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


    @OneToMany(mappedBy = "boss")
    @JsonIgnore
    private List<Subscription> subscriptionList;

}
