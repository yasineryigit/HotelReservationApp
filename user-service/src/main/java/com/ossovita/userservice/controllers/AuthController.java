package com.ossovita.userservice.controllers;

import com.ossovita.userservice.core.entities.RefreshToken;
import com.ossovita.userservice.core.entities.User;
import com.ossovita.userservice.core.utilities.error.RefreshTokenException;
import com.ossovita.userservice.core.entities.dto.request.LoginRequest;
import com.ossovita.userservice.core.entities.dto.request.TokenRefreshRequest;
import com.ossovita.userservice.core.entities.dto.response.JWTResponse;
import com.ossovita.userservice.core.entities.dto.response.TokenRefreshResponse;
import com.ossovita.userservice.security.CustomUserDetails;
import com.ossovita.userservice.security.jwt.JwtUtils;
import com.ossovita.userservice.security.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/1.0/user/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        log.info("Login request received: " + loginRequest.toString());
        String userEmail = loginRequest.getUserEmail();
        String userPassword = loginRequest.getUserPassword();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userEmail, userPassword);

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails.getUsername());

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUserPk());

        JWTResponse jwtResponse = new JWTResponse();


        jwtResponse.setUserEmail(userDetails.getUsername());
        jwtResponse.setUserPk(userDetails.getUserPk());
        jwtResponse.setToken(jwt);
        jwtResponse.setRefreshToken(refreshToken.getToken());
        jwtResponse.setRoles(roles);

        //TODO there is no need to return roles, instead return additionalData

        return ResponseEntity.ok(jwtResponse);

    }


    /*
        TODO fix | expired refresh token should return an exception and user should provide his credentials again to get new access token
    * */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        RefreshToken token = refreshTokenService.findByToken(requestRefreshToken)
                .orElseThrow(() -> new RefreshTokenException(requestRefreshToken + "Refresh token is not in database!"));

        RefreshToken deletedToken = refreshTokenService.verifyExpiration(token);

        User userRefreshToken = deletedToken.getUser();

        String newToken = jwtUtils.generateTokenFromUserEmail(userRefreshToken.getUserEmail());

        return ResponseEntity.ok(new TokenRefreshResponse(newToken, requestRefreshToken));


    }
}
