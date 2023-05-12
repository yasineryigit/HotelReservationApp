package com.ossovita.userservice.controller;

import com.ossovita.userservice.entity.RefreshToken;
import com.ossovita.userservice.entity.User;
import com.ossovita.userservice.payload.request.LoginRequest;
import com.ossovita.userservice.payload.request.TokenRefreshRequest;
import com.ossovita.userservice.payload.response.AuthResponse;
import com.ossovita.userservice.payload.response.TokenRefreshResponse;
import com.ossovita.userservice.utils.vm.BossVM;
import com.ossovita.userservice.utils.vm.CustomerVM;
import com.ossovita.userservice.utils.vm.EmployeeVM;
import com.ossovita.userservice.error.exception.RefreshTokenException;
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

import javax.validation.Valid;
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
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
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

        log.info("User roles: " + roles.stream().toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUser().getUserPk());

        AuthResponse authResponse = new AuthResponse();

        authResponse.setUserFirstName(userDetails.getUser().getUserFirstName());
        authResponse.setUserLastName(userDetails.getUser().getUserLastName());
        authResponse.setUserEmail(userDetails.getUser().getUserEmail());
        authResponse.setUserPk(userDetails.getUser().getUserPk());
        authResponse.setToken(jwt);
        authResponse.setRefreshToken(refreshToken.getToken());
        if (userDetails.getUser().getCustomer() != null) {//if user is a customer
            CustomerVM customerVM = new CustomerVM();
            customerVM.setCustomerFk(userDetails.getUser().getCustomer().getCustomerPk());
            authResponse.setAdditionalData(customerVM);
        }
        if (userDetails.getUser().getEmployee() != null) {//if user is a employee
            EmployeeVM employeeVM = new EmployeeVM();
            employeeVM.setEmployeeFk(userDetails.getUser().getEmployee().getEmployeePk());
            employeeVM.setBusinessPositionFk(userDetails.getUser().getEmployee().getBusinessPosition().getBusinessPositionPk());
            authResponse.setAdditionalData(employeeVM);
        }
        if (userDetails.getUser().getBoss() != null) {//if user is a boss
            BossVM bossVM = new BossVM();
            bossVM.setBossFk(userDetails.getUser().getBoss().getBossPk());
            authResponse.setAdditionalData(bossVM);
        }


        return ResponseEntity.ok(authResponse);

    }


    /*
        TODO fix | expired refresh token should return an exception and user should provide his credentials again to get new access token
    * */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        RefreshToken token = refreshTokenService.findByToken(requestRefreshToken)
                .orElseThrow(() -> new RefreshTokenException(requestRefreshToken + "Refresh token is not in database!"));

        RefreshToken deletedToken = refreshTokenService.verifyExpiration(token);

        User userRefreshToken = deletedToken.getUser();

        String newToken = jwtUtils.generateTokenFromUserEmail(userRefreshToken.getUserEmail());

        return ResponseEntity.ok(new TokenRefreshResponse(newToken, requestRefreshToken));


    }
}
