package com.ossovita.accountingservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaInfo());
    }


    private ApiInfo metaInfo() {

        return new ApiInfo(
                "Hotel Reservation App",
                "Spring Boot",
                "1.0",
                "Terms of Service",
                new Contact("Ossovita", "www.github.com/yasineryigit",
                        "ossovita@gmail.com").toString(),
                "Apache License Version 2.0",
                "https://www.apache.org/license.html"
        );
    }
}