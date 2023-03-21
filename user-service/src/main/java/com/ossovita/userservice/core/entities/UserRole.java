package com.ossovita.userservice.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "user_roles")
@AllArgsConstructor
@NoArgsConstructor
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