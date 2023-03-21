package com.ossovita.userservice.security.auth;

import com.ossovita.userservice.core.entities.vm.UserVM;
import lombok.Data;

@Data
public class AuthResponse<T> {

    private String token;

    private UserVM user;

    private T additionalData;


}
