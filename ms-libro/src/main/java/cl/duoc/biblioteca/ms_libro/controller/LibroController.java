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

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<LibroResponse>>> listar(
            @RequestHeader("Authorization") String token) {
        List<LibroResponse> libros = libroService.listarTodos();
        ApiResponse<List<LibroResponse>> response = new ApiResponse<>(true, "Listado obtenido", libros, null);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<LibroResponse>> crear(
            @RequestBody LibroDTO dto,
            @RequestHeader("Authorization") String token) {
        Libro nuevoLibro = new Libro(dto.getTitulo(), dto.getIdAutor());
        LibroResponse libroGuardado = libroService.crearLibro(nuevoLibro);
        
        ApiResponse<LibroResponse> response = new ApiResponse<>(true, "Libro creado exitosamente", libroGuardado, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}