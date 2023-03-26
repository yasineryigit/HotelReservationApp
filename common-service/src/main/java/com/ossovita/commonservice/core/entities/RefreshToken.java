package com.ossovita.commonservice.core.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "RefreshToken")
@Data
public class RefreshToken implements Serializable {

    @Id
    @SequenceGenerator(name = "refresh_token_seq", allocationSize = 1)
    @GeneratedValue(generator = "refresh_token_seq")
    @Column(name = "refresh_token_pk")
    private long refreshTokenPk;

    @OneToOne
    //todo check
    @JoinColumn(name = "user_pk", referencedColumnName = "user_pk")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;


}
