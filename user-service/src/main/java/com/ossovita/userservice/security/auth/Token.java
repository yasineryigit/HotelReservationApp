package com.ossovita.userservice.security.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ossovita.userservice.core.entities.User;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Token {

    @Id
    private String token;

    @ManyToOne
    @JsonIgnore
    private User user;


}
