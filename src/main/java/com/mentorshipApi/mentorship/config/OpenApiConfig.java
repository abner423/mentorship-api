package com.mentorshipApi.mentorship.config;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI mentorshipOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mentorship API")
                        .description("Simple mentorship API with Java, Spring Boot and JPA")
                        .version("v1.0"));
    }
}
