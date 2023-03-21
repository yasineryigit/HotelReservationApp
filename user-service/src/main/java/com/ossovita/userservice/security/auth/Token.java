package com.ossovita.userservice.security.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ossovita.userservice.core.entities.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Token {

    @Id
    private String token;

    @ManyToOne
    @JsonIgnore
    private User user;


}
