package com.clinicasc.api.agendamento.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration 
public class SwaggerConfig {
    
    @Bean 
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Clínica API")
                        .version("v1.0.0")
                        .description("API para gestão de consultas, pacientes e médicos.")
                        .contact(new Contact()
                                .name("Swagger da API")));
    }
}
