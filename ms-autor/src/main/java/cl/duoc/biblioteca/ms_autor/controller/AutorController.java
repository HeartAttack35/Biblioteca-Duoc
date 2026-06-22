package cl.duoc.biblioteca.ms_autor.controller;

import cl.duoc.biblioteca.ms_autor.dto.ApiResponse;
import cl.duoc.biblioteca.ms_autor.dto.AutorDTO;
import cl.duoc.biblioteca.ms_autor.model.Autor;
import cl.duoc.biblioteca.ms_autor.service.AutorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autores")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Autor>>> listar() {
        List<Autor> autores = autorService.listarTodos();
        ApiResponse<List<Autor>> response = new ApiResponse<>(true, "Listado obtenido", autores, null);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Autor>> obtenerPorId(@PathVariable Integer id) {
        return autorService.buscarPorId(id)
                .map(autor -> ResponseEntity.ok(new ApiResponse<>(true, "Autor encontrado", autor, null)))
                .orElse(ResponseEntity.status(404)
                        .body(new ApiResponse<>(false, "Autor no encontrado", null, "NOT_FOUND")));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<Autor>> crear(@RequestBody AutorDTO dto) {
        Autor nuevoAutor = new Autor(dto.getNombre());
        Autor autorGuardado = autorService.crearAutor(nuevoAutor);
        
        ApiResponse<Autor> response = new ApiResponse<>(true, "Autor creado exitosamente", autorGuardado, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}