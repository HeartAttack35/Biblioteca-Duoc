package cl.duoc.biblioteca.ms_libro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${ms.autor.url:http://localhost:8083}")
    private String msAutorUrl;

    /**
     * Bean de WebClient configurado para comunicarse con ms-autor.
     * La URL base se puede sobreescribir vía variable de entorno MS_AUTOR_URL.
     */
    @Bean
    public WebClient autorWebClient() {
        return WebClient.builder()
                .baseUrl(msAutorUrl)
                .build();
    }
}
