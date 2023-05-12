package com.ossovita.userservice.security;

import com.ossovita.userservice.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class CustomUserDetails implements UserDetails {

    private User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getUserRole().getUserRole()));

        if (user.getEmployee() != null) {//eğer bir employee ise
            authorities.add(new SimpleGrantedAuthority(user.getEmployee().getBusinessPosition().getBusinessPositionName()));
        }
        if (user.getBoss() != null) {//eğer bir employee ise
            authorities.add(new SimpleGrantedAuthority(user.getBoss().getBusinessPosition().getBusinessPositionName()));
        }
        System.out.println("authorities:" + authorities);
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getUserPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }



}
