package com.ossovita.userservice.core.utilities.constants;

public final class SecurityConstants {
    public static final String CREATE_EMPLOYEE_URL = "/api/1.0/user/employees/create-employee-with-user-with-employeeposition-with-hotelemployees";
    public static final String CREATE_BOSS_URL = "/api/1.0/user/employees/create-boss";
    public static final String CREATE_CUSTOMER_URL = "/api/1.0/user/customers/create-customer";
    public static final String LOGIN_URL = "/api/1.0/user/auth/login";
    public static final String REFRESH_TOKEN_URL = "/api/1.0/user/auth/refresh-token";

    // private constructor to prevent instantiation
    private SecurityConstants() {}
}