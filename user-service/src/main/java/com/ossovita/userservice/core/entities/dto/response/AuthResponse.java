package com.ossovita.userservice.core.entities.dto.response;

import lombok.Data;

@Data
public class AuthResponse<T> {
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private long userPk;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private T additionalData;


}