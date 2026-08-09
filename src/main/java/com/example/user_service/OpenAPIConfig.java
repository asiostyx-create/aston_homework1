package com.example.user_service;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI defineOpenAPI (
        @Value("${app.title}") String title,
        @Value("${app.description}") String description,
        @Value("${app.contact.name}") String contactName,
        @Value("${app.contact.email}") String contactEmail) {

            return new OpenAPI()
                    .info(new Info()
                            .title(title)
                            .description(description)
                            .contact(new Contact()
                                    .name(contactName)
                                    .email(contactEmail)));
        }
    }
