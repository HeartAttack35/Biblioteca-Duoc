package cl.duoc.biblioteca.ms_libro.controller;

import cl.duoc.biblioteca.ms_libro.dto.ApiResponse;
import cl.duoc.biblioteca.ms_libro.dto.LibroDTO;
import cl.duoc.biblioteca.ms_libro.dto.LibroResponse;
import cl.duoc.biblioteca.ms_libro.model.Libro;
import cl.duoc.biblioteca.ms_libro.service.LibroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    /**
     * Lista todos los libros. El token JWT se reenvía a ms-autor para
     * resolver el nombre del autor de cada libro en tiempo real.
     */
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<LibroResponse>>> listar(
            @RequestHeader("Authorization") String token) {
        // Se pasa el token para que LibroService pueda autenticarse ante ms-autor
        List<LibroResponse> libros = libroService.listarTodosConToken(token);
        ApiResponse<List<LibroResponse>> response = new ApiResponse<>(true, "Listado obtenido", libros, null);
        return ResponseEntity.ok(response);
    }

    /**
     * Crea un libro nuevo. El token se reenvía a ms-autor para incluir
     * los datos del autor en la respuesta.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<LibroResponse>> crear(
            @RequestBody LibroDTO dto,
            @RequestHeader("Authorization") String token) {
        Libro nuevoLibro = new Libro(dto.getTitulo(), dto.getIdAutor());
        LibroResponse libroGuardado = libroService.crearLibroConToken(nuevoLibro, token);

        ApiResponse<LibroResponse> response = new ApiResponse<>(true, "Libro creado exitosamente", libroGuardado, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
