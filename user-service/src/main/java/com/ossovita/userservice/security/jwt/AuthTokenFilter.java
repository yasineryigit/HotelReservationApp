package com.ossovita.userservice.security.jwt;

import com.ossovita.userservice.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);


    private final JwtUtils jwtUtils;

    private final CustomUserDetailsService customUserDetailsService;

    //if there is validated token, set authenticated
    //if no, let the request continue to the target destination
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            logger.error("AuthTokenFilter | doFilterInternal | jwt: {}", jwt);

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }


        } catch (Exception e) {
            logger.error("AuthTokenFilter | doFilterInternal | Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    //parse jwt from the httpservletrequest
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        logger.info("AuthTokenFilter | parseJwt | headerAuth: {}", headerAuth);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {

            logger.info("AuthTokenFilter | parseJwt | parseJwt: {}", headerAuth.substring(7, headerAuth.length()));

            return headerAuth.substring(7, headerAuth.length());
        }

        return null;
    }
}
