package com.ejunior.fisio_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI().components(new Components().addSecuritySchemes("security", securityScheme()))
                .info(new Info().title("API Gestão Fisio").description("API para gestão de empresa que presta serviços terceirizados de fisioterapia")
                .version("V1").license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")));
    }


    private SecurityScheme securityScheme(){
        return new SecurityScheme().description("Enter a valid token to use the resources")
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("security");
    }
}
