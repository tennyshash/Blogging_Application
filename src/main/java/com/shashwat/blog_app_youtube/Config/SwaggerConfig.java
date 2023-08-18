package com.shashwat.blog_app_youtube.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SwaggerConfig {

    private SecurityScheme createAPIKeyScheme(){
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("Bearer");
    }

    @Bean
    public OpenAPI usersMicroserviceOpenAPI() {

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes("Bearer Authentication",createAPIKeyScheme()))
                .info(new Info().title("Blogging Application ")
                        .description("This Project Is Developed By Shashwat Pratap Singh Parihar")
                        .version("1.0")
                        .termsOfService("Terms of Service")
                        .contact(new Contact().name("Shashwat Pratap").email("shashwatpratap@gmail.com"))
                        .license( new License().name("License of API"))  )
                .servers(Collections.singletonList(new Server().url("http://localhost:9999/")));
    }
}
