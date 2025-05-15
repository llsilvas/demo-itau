package br.dev.ldemo.itau.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${app.version}")
    private String version = "1.0.0";

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Demo Itau API")
                        .description("Teste de conceito entrevista Leandro Silva")
                        .version(version)
                        .contact(new Contact()
                                .name("Leandro Silva").email("lsilva.info@gmail.com")));
    }
}
