package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.assemblers.CarreraModelAssembler;
import cl.duoc.biblioteca.model.Carrera;
import cl.duoc.biblioteca.service.CarreraService;
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
@RequestMapping("/api/v2/carreras")
@Tag(name = "Carreras V2", description = "Consulta de carreras académicas con soporte HATEOAS")
public class CarreraControllerV2 {

    @Autowired
    private CarreraService carreraService;

    @Autowired
    private CarreraModelAssembler assembler;

    @Operation(
        summary = "Listar todas las carreras",
        description = "Retorna el listado completo de carreras académicas registradas. La respuesta incluye enlaces HATEOAS a cada recurso."
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
                        "carreraList": [
                          { "codigo": "ING-INF", "nombre": "Ingeniería en Informática",
                            "_links": { "self": { "href": "/api/v2/carreras/ING-INF" } } }
                        ]
                      },
                      "_links": { "self": { "href": "/api/v2/carreras" } }
                    }
                    """)
            )
        )
    })
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Carrera>> getAllCarreras() {
        List<EntityModel<Carrera>> carreras = carreraService.findAll().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(carreras,
            linkTo(methodOn(CarreraControllerV2.class).getAllCarreras()).withSelfRel()
        );
    }

    @Operation(
        summary = "Obtener carrera por código",
        description = "Retorna una carrera académica identificada por su código único."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Carrera encontrada",
            content = @Content(
                mediaType = MediaTypes.HAL_JSON_VALUE,
                examples = @ExampleObject(value = """
                    {
                      "codigo": "ING-INF",
                      "nombre": "Ingeniería en Informática",
                      "_links": { "self": { "href": "/api/v2/carreras/ING-INF" } }
                    }
                    """)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Carrera no encontrada", content = @Content)
    })
    @GetMapping(value = "/{codigo}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Carrera> getCarreraByCodigo(
            @Parameter(description = "Código único de la carrera", example = "ING-INF", required = true)
            @PathVariable String codigo) {
        Carrera carrera = carreraService.findById(codigo);
        return assembler.toModel(carrera);
    }
}
