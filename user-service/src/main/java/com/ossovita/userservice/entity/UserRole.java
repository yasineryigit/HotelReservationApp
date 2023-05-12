package com.ossovita.userservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "user_roles")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRole {


    @Id
    @SequenceGenerator(name = "user_role_seq", allocationSize = 1)
    @GeneratedValue(generator = "user_role_seq")
    @Column(name = "user_role_pk")
    private long userRolePk;

    @Column(name = "user_role")
    private String userRole;

    @OneToMany(mappedBy = "userRole")
    @JsonIgnore
    private List<User> users;


}
