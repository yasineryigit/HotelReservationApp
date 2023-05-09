package com.ossovita.hotelservice.security.jwt;

import com.netflix.discovery.converters.Auto;
import com.ossovita.hotelservice.security.config.RouteValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    RouteValidator routeValidator;


    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {

        log.info("JwtAuthenticationFilter | filter | isApiSecured.test(request) : " + routeValidator.isSecured.test(req));

        try {

            String jwt = parseJwt(req);
            log.info("Hotel Service | JwtAuthenticationFilter | doFilterInternal | jwt: {}", jwt);

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {

                String userEmail = jwtUtils.getUserEmailFromJwtToken(jwt);
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();

                for (String rolename : jwtUtils.getRoleNamesFromJwtToken(jwt)) {
                    authorities.add(new SimpleGrantedAuthority(rolename));
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userEmail, null, authorities
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }


        } catch (Exception e) {
            log.error("Hotel Service | JwtAuthenticationFilter | doFilterInternal | Cannot set user authentication: {}", e.getMessage());
        }

        chain.doFilter(req, res);
    }

    //parse jwt from the httpservletrequest
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        log.info("Hotel Service | JwtAuthenticationFilter | parseJwt | headerAuth: {}", headerAuth);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {

            log.info("Hotel Service | JwtAuthenticationFilter | parseJwt: {}", headerAuth.substring(7, headerAuth.length()));

            return headerAuth.substring(7);
        }

        return null;
    }
}
