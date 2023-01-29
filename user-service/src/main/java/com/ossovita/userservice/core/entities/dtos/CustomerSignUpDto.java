package com.ossovita.userservice.core.entities.dtos;

import com.ossovita.userservice.core.utilities.validators.UniqueUserEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerSignUpDto {

    private long customerPk;

    //user
    @NotNull(message = "{ossovita.constraint.email.NotNull.message}")
    @Size(min = 4, max = 255)
    @Email(message = "Email should be valid")
    @UniqueUserEmail
    private String userEmail;

    @NotNull
    @Size(min = 8, max = 255)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "{ossovita.constraint.password.Pattern.message}")
    private String userPassword;

    @NotNull
    @Size(min = 1, max = 255)
    private String userFirstName;

    @NotNull
    @Size(min = 1, max = 255)
    private String userLastName;

    private long userRoleFk;





}
