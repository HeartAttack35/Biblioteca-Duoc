package cl.duoc.biblioteca.ms_libro.service;

import cl.duoc.biblioteca.ms_libro.dto.AutorDTO;
import cl.duoc.biblioteca.ms_libro.dto.LibroResponse;
import cl.duoc.biblioteca.ms_libro.model.Libro;
import cl.duoc.biblioteca.ms_libro.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibroService {

    private final LibroRepository libroRepository;
    private final WebClient autorWebClient;

    public LibroService(LibroRepository libroRepository,
                        @Qualifier("autorWebClient") WebClient autorWebClient) {
        this.libroRepository = libroRepository;
        this.autorWebClient = autorWebClient;
    }

    @Transactional(readOnly = true)
    public List<LibroResponse> listarTodos() {
        return listarTodosConToken(null);
    }

    /**
     * Lista todos los libros consultando el nombre del autor a ms-autor.
     * El token JWT del usuario se reenvía al microservicio remoto.
     *
     * @param token Token JWT con prefijo "Bearer " obtenido del header Authorization
     */
    @Transactional(readOnly = true)
    public List<LibroResponse> listarTodosConToken(String token) {
        return libroRepository.findAll().stream().map(libro -> {
            AutorDTO autor = buscarAutorRemoto(libro.getIdAutor(), token);
            return new LibroResponse(libro.getId(), libro.getTitulo(), autor);
        }).collect(Collectors.toList());
    }

    @Transactional
    public LibroResponse crearLibro(Libro libro) {
        return crearLibroConToken(libro, null);
    }

    /**
     * Crea un libro y resuelve el autor remotamente para incluirlo en la respuesta.
     *
     * @param libro Entidad libro a persistir
     * @param token Token JWT con prefijo "Bearer " para consultar ms-autor
     */
    @Transactional
    public LibroResponse crearLibroConToken(Libro libro, String token) {
        Libro libroGuardado = libroRepository.save(libro);
        AutorDTO autor = buscarAutorRemoto(libroGuardado.getIdAutor(), token);
        return new LibroResponse(libroGuardado.getId(), libroGuardado.getTitulo(), autor);
    }

    /**
     * Consulta el microservicio ms-autor para obtener los datos del autor por su ID.
     * Si la comunicación falla (servicio caído, autor no encontrado, token inválido),
     * retorna un AutorDTO con datos de fallback para no romper la respuesta del libro.
     *
     * @param idAutor ID del autor a consultar
     * @param token   Token JWT para autenticarse ante ms-autor (puede ser null)
     * @return AutorDTO con los datos del autor, o un fallback en caso de error
     */
    private AutorDTO buscarAutorRemoto(Integer idAutor, String token) {
        try {
            WebClient.RequestHeadersSpec<?> request = autorWebClient
                    .get()
                    .uri("/api/autores/{id}", idAutor);

            // Solo reenviar el token si está disponible
            if (token != null && !token.isBlank()) {
                request = ((WebClient.RequestHeadersUriSpec<?>) autorWebClient
                        .get()
                        .uri("/api/autores/{id}", idAutor))
                        .header("Authorization", token);
            }

            AutorResponse respuesta = request
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                            clientResponse.createException()
                    )
                    .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                            clientResponse.createException()
                    )
                    .bodyToMono(new ParameterizedTypeReference<AutorResponse>() {})
                    .block();

            if (respuesta != null && respuesta.isSuccess() && respuesta.getData() != null) {
                return new AutorDTO(respuesta.getData().getId(), respuesta.getData().getNombre());
            }

        } catch (Exception e) {
            // Si ms-autor no responde o devuelve error, no fallamos el listado de libros.
            // Se registra el error y se retorna un fallback descriptivo.
            System.err.println("Error al consultar ms-autor para idAutor=" + idAutor + ": " + e.getMessage());
        }

        // Fallback: datos mínimos para que el libro siga siendo visible
        return new AutorDTO(idAutor, "Autor no disponible (id: " + idAutor + ")");
    }

    /**
     * DTO interno que modela la estructura de respuesta de ms-autor:
     * { "success": true, "data": { "id": 1, "nombre": "..." } }
     */
    private static class AutorResponse {
        private boolean success;
        private AutorData data;

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }

        public AutorData getData() { return data; }
        public void setData(AutorData data) { this.data = data; }
    }

    private static class AutorData {
        private Integer id;
        private String nombre;

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }
}
