package com.ossovita.userservice.core.utilities.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class JWTResponse {
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private long userPk;
    private String userEmail;
    //TODO | rempve roles, add additionalData

    private List<String> roles;
}