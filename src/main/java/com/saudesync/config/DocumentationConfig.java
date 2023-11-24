package com.saudesync.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentationConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SaudeSync API")
                        .contact(new Contact().email("saudesync@gmail.com").name("Lucas Sabonaro"))
                        .version("v1")
                        .description("Explore a poderosa API SaudeSync, projetada para melhorar o monitoramento e cuidado da saúde.")
                )
                .components(new Components()
                        .addSecuritySchemes("Bearer Auth",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer")
                                        .bearerFormat("JWT"))
                );
    }

}
