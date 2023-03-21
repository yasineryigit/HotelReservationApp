package com.ossovita.userservice.security.auth;

import com.ossovita.userservice.core.utilities.shared.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/1.0")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/auth/user")
    AuthResponse handleUserAuthentication(@RequestBody Credentials credentials){
        return authService.authenticateUser(credentials);
    }

    @PostMapping("/logout")
    GenericResponse handleLogout(@RequestHeader(name = "Authorization") String authorization){
        String token = authorization.substring(7);
        authService.clearToken(token);
        return new GenericResponse("Logout success");
    }



}