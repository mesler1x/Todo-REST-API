package com.roslabsystem.todo.configuration;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(
                        new Info()
                                .title("RosLabSystem TODO Rest API")
                                .description("Todo REST API RosLabSystem task")
                                .version("1.0")
                );
    }
}