package com.ossovita.apigateway.config;

import com.ossovita.apigateway.constants.SecurityConstants;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    //non-secured api endpoints
    public static final List<String> openApiEndpoints = Arrays.asList(
            SecurityConstants.CREATE_BOSS_URL,
            SecurityConstants.CREATE_CUSTOMER_URL,
            SecurityConstants.LOGIN_URL,
            SecurityConstants.REFRESH_TOKEN_URL
    );

    //filter non-secured api endpoints from the request
    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
