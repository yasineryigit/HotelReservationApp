package com.ossovita.accountingservice.utils.constants;

import java.util.Arrays;
import java.util.List;

public final class SecurityConstants {

    public static final String ACCOUNTING_SERVICE_SWAGGER_UI_URL = "/api/1.0/accounting/swagger-ui/**";

    // private constructor to prevent instantiation
    private SecurityConstants() {
    }

    public static List<String> getIgnoringUrls() {
        System.out.println("getIgnoringUrls");
        return Arrays.asList(
                ACCOUNTING_SERVICE_SWAGGER_UI_URL
        );
    }
}