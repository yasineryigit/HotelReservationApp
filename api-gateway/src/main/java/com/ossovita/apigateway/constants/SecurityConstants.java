package com.ossovita.apigateway.constants;

import java.util.Arrays;
import java.util.List;

public final class SecurityConstants {
    public static final String CREATE_BOSS_URL = "/api/1.0/user/boss/create-boss";
    public static final String CREATE_CUSTOMER_URL = "/api/1.0/user/customers/create-customer";
    public static final String LOGIN_URL = "/api/1.0/user/auth/login";
    public static final String REFRESH_TOKEN_URL = "/api/1.0/user/auth/refresh-token";
    public static final String USER_SERVICE_SWAGGER_UI_URL = "/api/1.0/user/swagger-ui";
    public static final String ACCOUNTING_SERVICE_SWAGGER_UI_URL = "/api/1.0/accounting/swagger-ui";

    // private constructor to prevent instantiation
    private SecurityConstants() {
    }

    //non-secured api endpoints
    public static final List<String> openApiEndpoints = Arrays.asList(
            CREATE_BOSS_URL,
            CREATE_CUSTOMER_URL,
            LOGIN_URL,
            REFRESH_TOKEN_URL,
            USER_SERVICE_SWAGGER_UI_URL,
            ACCOUNTING_SERVICE_SWAGGER_UI_URL
    );
}
