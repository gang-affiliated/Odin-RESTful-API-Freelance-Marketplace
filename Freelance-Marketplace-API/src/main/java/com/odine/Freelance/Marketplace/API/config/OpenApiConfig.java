package com.odine.Freelance.Marketplace.API.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI freelanceMarketplaceOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Odine Freelance Marketplace API")
                .description("REST API for freelancer, job, and comment management with async freelancer evaluation.")
                .version("v1")
                .contact(new Contact().name("Odine Backend Team"))
                .license(new License().name("Internal Assessment Project")));
    }
}
