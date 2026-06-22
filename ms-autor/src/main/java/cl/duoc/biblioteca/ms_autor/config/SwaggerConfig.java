package cl.duoc.biblioteca.ms_autor.config;

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

    // URL base desde la que se accede a la API.
    // En local apunta al Gateway (8080). Puede sobreescribirse vía variable de entorno.
    @Value("${app.swagger.server-url:http://localhost:8080}")
    private String serverUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        // Esquema de seguridad Bearer JWT para que Swagger muestre el botón "Authorize"
        SecurityScheme bearerScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Introduce el token JWT obtenido desde POST /auth/login");

        return new OpenAPI()
                .servers(List.of(
                        new Server()
                                .url(serverUrl)
                                .description("API Gateway — punto de entrada principal")
                ))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", bearerScheme))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .info(new Info()
                        .title("API Autores — Biblioteca Duoc UC")
                        .version("1.0")
                        .description("""
                                Microservicio encargado de la gestión de autores de libros.
                                Todos los endpoints requieren autenticación mediante token JWT.
                                Los endpoints de creación requieren rol ADMIN.
                                """)
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
