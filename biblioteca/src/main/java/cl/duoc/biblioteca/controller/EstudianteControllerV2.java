package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.assemblers.EstudianteModelAssembler;
import cl.duoc.biblioteca.model.Estudiante;
import cl.duoc.biblioteca.service.EstudianteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/estudiantes")
@Tag(name = "Estudiantes V2", description = "Consulta de estudiantes con soporte HATEOAS")
public class EstudianteControllerV2 {

    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private EstudianteModelAssembler assembler;

    @Operation(
        summary = "Listar todos los estudiantes",
        description = "Retorna el listado completo de estudiantes registrados en el sistema. Cada elemento incluye enlaces HATEOAS."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Listado obtenido exitosamente",
            content = @Content(
                mediaType = MediaTypes.HAL_JSON_VALUE,
                examples = @ExampleObject(value = """
                    {
                      "_embedded": {
                        "estudianteList": [
                          {
                            "id": 1, "run": "12345678-9", "nombres": "Juan Pérez",
                            "correo": "juan.perez@duoc.cl", "jornada": "D", "telefono": 912345678,
                            "_links": { "self": { "href": "/api/v2/estudiantes/1" } }
                          }
                        ]
                      },
                      "_links": { "self": { "href": "/api/v2/estudiantes" } }
                    }
                    """)
            )
        )
    })
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Estudiante>> getAllEstudiantes() {
        List<EntityModel<Estudiante>> estudiantes = estudianteService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(estudiantes,
                linkTo(methodOn(EstudianteControllerV2.class).getAllEstudiantes()).withSelfRel());
    }

    @Operation(
        summary = "Obtener estudiante por ID",
        description = "Retorna un estudiante específico identificado por su ID numérico autogenerado."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Estudiante encontrado",
            content = @Content(
                mediaType = MediaTypes.HAL_JSON_VALUE,
                examples = @ExampleObject(value = """
                    {
                      "id": 1, "run": "12345678-9", "nombres": "Juan Pérez",
                      "correo": "juan.perez@duoc.cl", "jornada": "D", "telefono": 912345678,
                      "_links": { "self": { "href": "/api/v2/estudiantes/1" } }
                    }
                    """)
            )
        ),
        @ApiResponse(responseCode = "500", description = "Estudiante no encontrado (RuntimeException)", content = @Content)
    })
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Estudiante> getEstudianteById(
            @Parameter(description = "ID numérico del estudiante", example = "1", required = true)
            @PathVariable Integer id) {
        Estudiante estudiante = estudianteService.findById(id);
        if (estudiante == null) {
            throw new RuntimeException("Estudiante no encontrado con ID: " + id);
        }
        return assembler.toModel(estudiante);
    }
}
