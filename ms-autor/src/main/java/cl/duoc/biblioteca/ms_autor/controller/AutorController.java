package cl.duoc.biblioteca.ms_autor.controller;

import cl.duoc.biblioteca.ms_autor.dto.ApiResponse;
import cl.duoc.biblioteca.ms_autor.dto.AutorDTO;
import cl.duoc.biblioteca.ms_autor.model.Autor;
import cl.duoc.biblioteca.ms_autor.service.AutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autores")
@Tag(name = "Autores", description = "Gestión de autores de libros. Requiere autenticación JWT.")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @Operation(
        summary = "Listar todos los autores",
        description = "Retorna la lista completa de autores registrados. Requiere rol USER o ADMIN.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Lista de autores obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "message": "Listado obtenido",
                      "data": [
                        { "id": 1, "nombre": "Gabriel García Márquez" },
                        { "id": 2, "nombre": "Isabel Allende" }
                      ],
                      "error": null
                    }
                    """)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Token JWT ausente o inválido",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = """
                    { "success": false, "message": "Unauthorized", "data": null, "error": "UNAUTHORIZED" }
                    """))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Rol insuficiente para acceder al recurso",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = """
                    { "success": false, "message": "Forbidden", "data": null, "error": "FORBIDDEN" }
                    """))
        )
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Autor>>> listar() {
        List<Autor> autores = autorService.listarTodos();
        ApiResponse<List<Autor>> response = new ApiResponse<>(true, "Listado obtenido", autores, null);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Obtener autor por ID",
        description = "Busca y retorna un autor específico por su identificador único. Requiere rol USER o ADMIN.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Autor encontrado",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "message": "Autor encontrado",
                      "data": { "id": 1, "nombre": "Gabriel García Márquez" },
                      "error": null
                    }
                    """)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "No existe un autor con el ID indicado",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = """
                    { "success": false, "message": "Autor no encontrado", "data": null, "error": "NOT_FOUND" }
                    """))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Token JWT ausente o inválido",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = """
                    { "success": false, "message": "Unauthorized", "data": null, "error": "UNAUTHORIZED" }
                    """))
        )
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Autor>> obtenerPorId(
            @Parameter(description = "ID numérico del autor", example = "1", required = true)
            @PathVariable Integer id) {
        return autorService.buscarPorId(id)
                .map(autor -> ResponseEntity.ok(new ApiResponse<>(true, "Autor encontrado", autor, null)))
                .orElse(ResponseEntity.status(404)
                        .body(new ApiResponse<>(false, "Autor no encontrado", null, "NOT_FOUND")));
    }

    @Operation(
        summary = "Crear nuevo autor",
        description = "Registra un nuevo autor en el sistema. Requiere rol ADMIN.",
        security = @SecurityRequirement(name = "bearerAuth"),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del autor a crear",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AutorDTO.class),
                examples = @ExampleObject(value = """
                    { "nombre": "Pablo Neruda" }
                    """)
            )
        )
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Autor creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "message": "Autor creado exitosamente",
                      "data": { "id": 3, "nombre": "Pablo Neruda" },
                      "error": null
                    }
                    """)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Token JWT ausente o inválido",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = """
                    { "success": false, "message": "Unauthorized", "data": null, "error": "UNAUTHORIZED" }
                    """))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Se requiere rol ADMIN para crear autores",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = """
                    { "success": false, "message": "Forbidden", "data": null, "error": "FORBIDDEN" }
                    """))
        )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<Autor>> crear(@RequestBody AutorDTO dto) {
        Autor nuevoAutor = new Autor(dto.getNombre());
        Autor autorGuardado = autorService.crearAutor(nuevoAutor);

        ApiResponse<Autor> response = new ApiResponse<>(true, "Autor creado exitosamente", autorGuardado, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
