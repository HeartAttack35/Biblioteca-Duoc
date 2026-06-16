package cl.duoc.biblioteca.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                    .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor Local (Desarrollo")
                    ))
                    .info(new Info()
                        .title("API 2826 Reservas de salas")
                        .version("1.0")
                        .description("Documentación de la API para el sistema de reserva de salas.")
                        .contact(new Contact()
                            .name("Equipo de Desarrollo Backend")
                            .email("contacto@biblioteca.cuoc.cl")
                        )
                    .license(new License()
                        .name("Apache 2.0")
                        .url("http://springdoc.org")
                    ));
    }
}
