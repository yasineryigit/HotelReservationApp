package com.ossovita.apigateway.configuration;

//TODO FIX https://stackoverflow.com/questions/71108572/cannot-access-api-docs-of-microservice-from-spring-cloud-gateway-failed-to-load
/*@Configuration
public class GatewayConfig {

    RouteLocatorBuilder.Builder apiDocJsonRoutes(RouteLocatorBuilder.Builder builder) {
        return builder
                .route(p -> p.path("/api/1.0/hotel/**").uri("hotel-service"));
    }

    RouteLocatorBuilder.Builder apiDocUIRoutes(RouteLocatorBuilder.Builder builder) {
        return builder
                .route(p -> swaggerUi(p, "hotel-service", "/api/1.0/hotel/swagger-config"));

    }


    private Buildable<Route> swaggerUi(PredicateSpec p, String service, String expectedValue) {
        return p.path("/swagger-ui/index.html").and().query("configUrl", expectedValue)
                .uri(service);
    }
}
*/