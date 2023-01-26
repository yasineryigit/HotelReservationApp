package com.ossovita.userservice.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ossovita.userservice.core.utilities.validators.UniqueUserEmail;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @SequenceGenerator(name = "user_seq", allocationSize = 1)
    @GeneratedValue(generator = "user_seq")
    @Column(name = "user_pk")
    private long userPk;

    @NotNull(message = "{ossovita.constraint.email.NotNull.message}")
    @Size(min = 4, max = 255)
    @Email(message = "Email should be valid")
    @Column(name = "user_email")
    @UniqueUserEmail
    private String userEmail;

    @NotNull
    @Size(min = 8, max = 255)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "{ossovita.constraint.password.Pattern.message}")
    @JsonIgnore
    @Column(name = "user_password")
    private String userPassword;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "user_first_name")
    private String userFirstName;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "user_last_name")
    private String userLastName;

    @ManyToOne
    @JoinColumn(name = "user_role_fk", insertable = false, updatable = false)
    @JsonIgnore
    private UserRole userRole;

    @Column(name = "user_role_fk")
    private long userRoleFk;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private Customer customer;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private Employee employee;


}
