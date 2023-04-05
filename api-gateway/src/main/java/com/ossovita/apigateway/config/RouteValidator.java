package com.ossovita.apigateway.config;

import com.ossovita.apigateway.constants.SecurityConstants;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class RouteValidator {


    //filter non-secured api endpoints from the request
    public Predicate<ServerHttpRequest> isSecured =
            request -> SecurityConstants.openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().startsWith(uri));

}
