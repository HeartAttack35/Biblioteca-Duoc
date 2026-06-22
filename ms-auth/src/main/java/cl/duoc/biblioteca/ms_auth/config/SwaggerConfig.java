package cl.duoc.biblioteca.ms_auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
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
        // ms-auth no requiere esquema de seguridad en Swagger porque es
        // precisamente el servicio que emite los tokens (sus endpoints son públicos).
        return new OpenAPI()
                .servers(List.of(
                        new Server()
                                .url(serverUrl)
                                .description("API Gateway — punto de entrada principal")
                ))
                .info(new Info()
                        .title("API Autenticación — Biblioteca Duoc UC")
                        .version("1.0")
                        .description("""
                                Microservicio encargado de la autenticación de usuarios mediante JWT.
                                Expone el endpoint POST /auth/login que valida credenciales y retorna
                                un accessToken (JWT) y un refreshToken para uso posterior.
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
