package com.ossovita.hotelservice.utils.constants;

import java.util.Arrays;
import java.util.List;

public final class SecurityConstants {
    public static final String GET_ALL_HOTELS_URL = "/api/1.0/hotel/get-all-hotels";
    public static final String SERVE_HOTEL_IMAGE = "/api/1.0/hotel/uploads/hotel-images/**";//matcher pattern works here
    public static final String HOTEL_SERVICE_SWAGGER_UI_URL = "/api/1.0/hotel/swagger-ui";


    // private constructor to prevent instantiation
    private SecurityConstants() {
    }

    public static List<String> getIgnoringUrls() {
        return Arrays.asList(
                GET_ALL_HOTELS_URL,
                SERVE_HOTEL_IMAGE,
                HOTEL_SERVICE_SWAGGER_UI_URL
        );
    }
}




