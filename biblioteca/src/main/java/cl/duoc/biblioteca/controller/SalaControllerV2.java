package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.assemblers.SalaModelAssembler;
import cl.duoc.biblioteca.model.Sala;
import cl.duoc.biblioteca.service.SalaService;
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
@RequestMapping("/api/v2/salas")
@Tag(name = "Salas V2", description = "Consulta de salas de la biblioteca con soporte HATEOAS")
public class SalaControllerV2 {

    @Autowired
    private SalaService salaService;

    @Autowired
    private SalaModelAssembler assembler;

    @Operation(
        summary = "Listar todas las salas",
        description = "Retorna el listado completo de salas disponibles en la biblioteca. Incluye capacidad, tipo de sala e instituto. Respuesta en formato HAL+JSON."
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
                        "salaList": [
                          {
                            "codigo": 1, "nombre": "Sala A101", "capacidad": 30, "idInstituto": 1,
                            "tipoSala": { "idTipo": 1, "nombre": "Laboratorio" },
                            "_links": { "self": { "href": "/api/v2/salas/1" } }
                          }
                        ]
                      },
                      "_links": { "self": { "href": "/api/v2/salas" } }
                    }
                    """)
            )
        )
    })
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Sala>> getAllSalas() {
        List<EntityModel<Sala>> salas = salaService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(salas,
                linkTo(methodOn(SalaControllerV2.class).getAllSalas()).withSelfRel());
    }

    @Operation(
        summary = "Obtener sala por código",
        description = "Retorna una sala específica identificada por su código numérico autogenerado."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Sala encontrada",
            content = @Content(
                mediaType = MediaTypes.HAL_JSON_VALUE,
                examples = @ExampleObject(value = """
                    {
                      "codigo": 1,
                      "nombre": "Sala A101",
                      "capacidad": 30,
                      "idInstituto": 1,
                      "tipoSala": { "idTipo": 1, "nombre": "Laboratorio" },
                      "_links": { "self": { "href": "/api/v2/salas/1" } }
                    }
                    """)
            )
        ),
        @ApiResponse(responseCode = "500", description = "Sala no encontrada", content = @Content)
    })
    @GetMapping(value = "/{codigo}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Sala> getSalaByCodigo(
            @Parameter(description = "Código numérico de la sala", example = "1", required = true)
            @PathVariable Integer codigo) {
        Sala sala = salaService.findById(codigo);
        if (sala == null) {
            throw new RuntimeException("Sala no encontrada con código: " + codigo);
        }
        return assembler.toModel(sala);
    }
}
