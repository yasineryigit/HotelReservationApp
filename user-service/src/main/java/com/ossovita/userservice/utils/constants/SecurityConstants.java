package com.ossovita.userservice.utils.constants;

import java.util.Arrays;
import java.util.List;

public final class SecurityConstants {
    public static final String CREATE_BOSS_URL = "/api/1.0/user/boss/create-boss";
    public static final String CREATE_CUSTOMER_URL = "/api/1.0/user/customers/create-customer";
    public static final String LOGIN_URL = "/api/1.0/user/auth/login";
    public static final String REFRESH_TOKEN_URL = "/api/1.0/user/auth/refresh-token";
    public static final String SWAGGER_UI_URL = "/swagger-ui";


    // private constructor to prevent instantiation
    private SecurityConstants() {
    }

    public static List<String> getIgnoringUrls() {
        return Arrays.asList(
                CREATE_BOSS_URL,
                CREATE_CUSTOMER_URL,
                LOGIN_URL,
                REFRESH_TOKEN_URL,
                SWAGGER_UI_URL
        );
    }
}