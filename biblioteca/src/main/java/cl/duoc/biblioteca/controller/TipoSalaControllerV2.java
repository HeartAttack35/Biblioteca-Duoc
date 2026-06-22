package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.assemblers.TipoSalaModelAssembler;
import cl.duoc.biblioteca.model.TipoSala;
import cl.duoc.biblioteca.service.TipoSalaService;
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
@RequestMapping("/api/v2/tipos-salas")
@Tag(name = "Tipos de Sala V2", description = "Consulta de tipos de sala disponibles en la biblioteca con soporte HATEOAS")
public class TipoSalaControllerV2 {

    @Autowired
    private TipoSalaService tipoSalaService;

    @Autowired
    private TipoSalaModelAssembler assembler;

    @Operation(
        summary = "Listar todos los tipos de sala",
        description = "Retorna todos los tipos de sala definidos en el sistema (ej: Laboratorio, Sala de Estudio, Auditorio). Respuesta en formato HAL+JSON."
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
                        "tipoSalaList": [
                          { "idTipo": 1, "nombre": "Laboratorio",
                            "_links": { "self": { "href": "/api/v2/tipos-salas/1" } } },
                          { "idTipo": 2, "nombre": "Sala de Estudio",
                            "_links": { "self": { "href": "/api/v2/tipos-salas/2" } } }
                        ]
                      },
                      "_links": { "self": { "href": "/api/v2/tipos-salas" } }
                    }
                    """)
            )
        )
    })
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<TipoSala>> getAllTipoSalas() {
        List<EntityModel<TipoSala>> tipos = tipoSalaService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(tipos,
                linkTo(methodOn(TipoSalaControllerV2.class).getAllTipoSalas()).withSelfRel());
    }

    @Operation(
        summary = "Obtener tipo de sala por ID",
        description = "Retorna un tipo de sala específico por su identificador numérico."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Tipo de sala encontrado",
            content = @Content(
                mediaType = MediaTypes.HAL_JSON_VALUE,
                examples = @ExampleObject(value = """
                    {
                      "idTipo": 1,
                      "nombre": "Laboratorio",
                      "_links": { "self": { "href": "/api/v2/tipos-salas/1" } }
                    }
                    """)
            )
        ),
        @ApiResponse(responseCode = "500", description = "Tipo de sala no encontrado", content = @Content)
    })
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<TipoSala> getTipoSalaById(
            @Parameter(description = "ID numérico del tipo de sala", example = "1", required = true)
            @PathVariable Integer id) {
        TipoSala tipo = tipoSalaService.findById(id);
        if (tipo == null) {
            throw new RuntimeException("Tipo de sala no encontrado con ID: " + id);
        }
        return assembler.toModel(tipo);
    }
}
