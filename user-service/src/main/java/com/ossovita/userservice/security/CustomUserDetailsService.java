package com.ossovita.userservice.security;

import com.ossovita.commonservice.core.entities.User;
import com.ossovita.userservice.business.abstracts.UserService;
import com.ossovita.userservice.core.utilities.error.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {

        User user = userService.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("UserEmail " + userEmail + " not found"));

        return new CustomUserDetails(user);
    }


}
