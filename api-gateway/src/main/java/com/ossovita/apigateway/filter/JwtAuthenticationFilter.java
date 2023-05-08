package com.ossovita.apigateway.filter;

import com.ossovita.apigateway.config.RouteValidator;
import com.ossovita.apigateway.exception.JwtTokenMalformedException;
import com.ossovita.apigateway.exception.JwtTokenMissingException;
import com.ossovita.apigateway.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter implements GatewayFilter {

    private final JwtUtils jwtUtils;
    private final RouteValidator routeValidator;

    /*
    *       1-) jwt validation (if any error with it, let the user know)
            2-) if there is no error, resolve the jwt and take the userEmail parameter
            3-) continue to the http request with the added userEmail header
    *
    * */

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        log.info("JwtAuthenticationFilter | filter is working");

        ServerHttpRequest request = exchange.getRequest();

        log.info("Upcoming request path: " + request.getURI().getPath());
        log.info("JwtAuthenticationFilter | filter | isApiSecured.test(request) : " + routeValidator.isSecured.test(request));

        if (routeValidator.isSecured.test(request)) {
            //if request doesn't contain Authorization header
            if (!request.getHeaders().containsKey("Authorization")) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            final String authorization = request.getHeaders().getOrEmpty("Authorization").get(0);
            final String token = authorization.replace("Bearer ", "");

            log.info("JwtAuthenticationFilter | filter | token : " + token);

            try {
                jwtUtils.validateJwtToken(token);

            } catch (ExpiredJwtException e) {
                log.info("JwtAuthenticationFilter | filter | ExpiredJwtException | error : " + e.getMessage());
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);

                return response.setComplete();

            } catch (IllegalArgumentException | JwtTokenMalformedException | JwtTokenMissingException
                     | SignatureException | UnsupportedJwtException e) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.BAD_REQUEST);

                return response.setComplete();
            }

            Claims claims = jwtUtils.getClaims(token);
            exchange.getRequest().mutate().header("userEmail", String.valueOf(claims.get("userEmail"))).build();

        }

        return chain.filter(exchange);
    }
}
