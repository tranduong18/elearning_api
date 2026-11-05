package com.duong.elearning_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    private static final String BEARER_SCHEME = "Bearer Authentication";

    @Value("${application.swagger.server-url}")
    private String serverUrl;

    @Value("${application.swagger.server-description}")
    private String serverDescription;

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME))
                .components(new Components().addSecuritySchemes(BEARER_SCHEME, bearerScheme()))
                .servers(List.of(new Server().url(serverUrl).description(serverDescription)))
                .info(new Info()
                        .title("E-Learning API Documentation")
                        .description("""
                                E-Learning Platform is a comprehensive online learning system \
                                built for software developers and students. It offers structured courses, \
                                interactive lessons, Q&A forums, technical blogs and premium content \
                                with integrated payment.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("E-Learning Team")
                                .email("support@elearning.vn")
                                .url("https://elearning.vn"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }

    private SecurityScheme bearerScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Paste your JWT token here (without the 'Bearer ' prefix).");
    }
}
