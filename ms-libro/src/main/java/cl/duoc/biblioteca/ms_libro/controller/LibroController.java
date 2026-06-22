package cl.duoc.biblioteca.ms_libro.controller;

import cl.duoc.biblioteca.ms_libro.dto.ApiResponse;
import cl.duoc.biblioteca.ms_libro.dto.LibroDTO;
import cl.duoc.biblioteca.ms_libro.dto.LibroResponse;
import cl.duoc.biblioteca.ms_libro.model.Libro;
import cl.duoc.biblioteca.ms_libro.service.LibroService;
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
@RequestMapping("/api/libros")
@Tag(name = "Libros", description = """
        Gestión de libros. Cada libro está asociado a un autor resuelto en tiempo real
        consultando ms-autor mediante comunicación REST. Requiere autenticación JWT.
        """)
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @Operation(
        summary = "Listar todos los libros con datos del autor",
        description = """
            Retorna la lista completa de libros. El nombre del autor se resuelve
            en tiempo real consultando ms-autor. Si ms-autor no responde, el campo
            autor aparecerá como null (comportamiento de fallback). Requiere rol USER o ADMIN.
            """,
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Lista de libros obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "message": "Listado obtenido",
                      "data": [
                        {
                          "id": 1,
                          "titulo": "Cien años de soledad",
                          "autor": { "nombre": "Gabriel García Márquez" }
                        }
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
    public ResponseEntity<ApiResponse<List<LibroResponse>>> listar(
            @Parameter(
                description = "Token JWT en formato 'Bearer {token}'. Se reenvía a ms-autor para validar identidad.",
                required = true,
                example = "Bearer eyJhbGciOiJIUzI1NiJ9..."
            )
            @RequestHeader("Authorization") String token) {
        List<LibroResponse> libros = libroService.listarTodosConToken(token);
        ApiResponse<List<LibroResponse>> response = new ApiResponse<>(true, "Listado obtenido", libros, null);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Crear nuevo libro",
        description = """
            Registra un nuevo libro vinculado a un autor existente en ms-autor.
            El autor es validado consultando ms-autor en tiempo real.
            Requiere rol ADMIN.
            """,
        security = @SecurityRequirement(name = "bearerAuth"),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del libro a crear: título y el ID del autor en ms-autor",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LibroDTO.class),
                examples = @ExampleObject(value = """
                    {
                      "titulo": "El amor en los tiempos del cólera",
                      "idAutor": 1
                    }
                    """)
            )
        )
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Libro creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "message": "Libro creado exitosamente",
                      "data": {
                        "id": 2,
                        "titulo": "El amor en los tiempos del cólera",
                        "autor": { "nombre": "Gabriel García Márquez" }
                      },
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
            description = "Se requiere rol ADMIN para crear libros",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = """
                    { "success": false, "message": "Forbidden", "data": null, "error": "FORBIDDEN" }
                    """))
        )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<LibroResponse>> crear(
            @RequestBody LibroDTO dto,
            @Parameter(
                description = "Token JWT en formato 'Bearer {token}'. Se reenvía a ms-autor.",
                required = true,
                example = "Bearer eyJhbGciOiJIUzI1NiJ9..."
            )
            @RequestHeader("Authorization") String token) {
        Libro nuevoLibro = new Libro(dto.getTitulo(), dto.getIdAutor());
        LibroResponse libroGuardado = libroService.crearLibroConToken(nuevoLibro, token);

        ApiResponse<LibroResponse> response = new ApiResponse<>(true, "Libro creado exitosamente", libroGuardado, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
