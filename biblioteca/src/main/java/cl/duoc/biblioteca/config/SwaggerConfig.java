package cl.duoc.biblioteca.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    // URL base desde la que se accede a la API.
    // En local apunta al Gateway (8080). Puede sobreescribirse vía variable de entorno.
    @Value("${app.swagger.server-url:http://localhost:8080}")
    private String serverUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server()
                                .url(serverUrl)
                                .description("API Gateway — punto de entrada principal")
                ))
                .info(new Info()
                        .title("API Reservas de Salas — Biblioteca Duoc UC")
                        .version("1.0")
                        .description("Documentación de la API para el sistema de reserva de salas de la Biblioteca.")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo Backend")
                                .email("contacto@biblioteca.duoc.cl")
                        )
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")
                        )
                );
    }
}
