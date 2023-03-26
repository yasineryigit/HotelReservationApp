package com.ossovita.commonservice.core.entities.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
public class EmployeeSaveFormDto {

    //User
    @NotNull(message = "{ossovita.constraint.email.NotNull.message}")
    @Size(min = 4, max = 255)
    @Email(message = "Email should be valid")
    private String userEmail;

    @NotNull
    @Size(min = 8, max = 255)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "{ossovita.constraint.password.Pattern.message}")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String userPassword;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "user_first_name")
    private String userFirstName;

    @NotNull
    @Size(min = 1, max = 255)
    private String userLastName;

    //employeeposition
    @NotNull
    private long employeePositionFk;

    //Hotel
    private long hotelFk;


}
