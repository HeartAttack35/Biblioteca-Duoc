package cl.duoc.biblioteca.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import cl.duoc.biblioteca.model.Carrera;
import cl.duoc.biblioteca.service.CarreraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/carreras")
@Tag(name = "Carreras", description = "Operaciones relacionadas a las carreras")
public class CarreraController {
    @Autowired
    private CarreraService carreraService;

    @GetMapping
    @Operation(summary = "Obtener todas las carreras")
    public List<Carrera> findAll() {
        return carreraService.findAll();
    }

    @GetMapping("/{codigo}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "404", description = "Carrera no encontrada")
    })
    public Carrera getCarreraByCodigo(
        @Parameter(description = "Código de la carrera", required = true) @PathVariable String codigo
    ) {
        return carreraService.findById(codigo);
    }

    @PostMapping
    @Operation(summary = "Crear una nueva carrera")
    public Carrera createCarrera(
        @RequestBody(description = "Carrera a crear", required = true) Carrera carrera
    ){
        return carreraService.save(carrera);
    }
}