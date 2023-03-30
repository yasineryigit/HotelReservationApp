package com.ossovita.apigateway.constants;

import java.util.Arrays;
import java.util.List;

public final class SecurityConstants {
    public static final String CREATE_BOSS_URL = "/api/1.0/user/employees/create-boss";
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
            SecurityConstants.CREATE_BOSS_URL,
            SecurityConstants.CREATE_CUSTOMER_URL,
            SecurityConstants.LOGIN_URL,
            SecurityConstants.REFRESH_TOKEN_URL,
            SecurityConstants.USER_SERVICE_SWAGGER_UI_URL,
            SecurityConstants.ACCOUNTING_SERVICE_SWAGGER_UI_URL
    );
}
