package com.ossovita.commonservice.core.entities.auth;

import com.ossovita.commonservice.core.entities.vm.UserVM;
import lombok.Data;

@Data
public class AuthResponse<T> {

    private String token;

    private UserVM user;

    private T additionalData;


}
