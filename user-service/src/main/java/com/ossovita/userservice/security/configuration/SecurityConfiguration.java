package com.ossovita.userservice.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import javax.management.MXBean;


@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/api/1.0/auth/**",
            "/api/1.0/user/status"

            // other public endpoints of your API may be appended to this array
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.exceptionHandling().authenticationEntryPoint(new AuthEntryPoint());
        http.cors();
        http.authorizeRequests()
                //.anyRequest().authenticated()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .logout()
                .permitAll().and();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(tokenFilter(), UsernamePasswordAuthenticationFilter.class);

    }


    @Bean
    TokenFilter tokenFilter() {
        return new TokenFilter();
    }
}
