package com.ossovita.hotelservice.security.config;

import com.ossovita.hotelservice.utils.constants.SecurityConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    //filter non-secured api endpoints from the request
    public Predicate<HttpServletRequest> isSecured =
            request -> SecurityConstants.getIgnoringUrls()
                    .stream()
                    .noneMatch(uri -> request.getRequestURI().startsWith(uri));

}