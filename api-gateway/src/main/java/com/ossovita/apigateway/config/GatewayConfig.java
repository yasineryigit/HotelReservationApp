package com.ossovita.apigateway.config;

import com.ossovita.apigateway.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtAuthenticationFilter filter;


    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/api/1.0/user/**").filters(f -> f.filter(filter)).uri("lb://user-service"))
                .route("hotel-service", r -> r.path("/api/1.0/hotel/**").filters(f -> f.filter(filter)).uri("lb://hotel-service"))
                .route("reservation-service", r -> r.path("/api/1.0/reservation/**").filters(f -> f.filter(filter)).uri("lb://reservation-service"))
                .route("accounting-service", r -> r.path("/api/1.0/accounting/**").filters(f -> f.filter(filter)).uri("lb://accounting-service")).build();
    }
}
