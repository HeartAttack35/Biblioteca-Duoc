package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.assemblers.ReservaModelAssembler;
import cl.duoc.biblioteca.model.Reserva;
import cl.duoc.biblioteca.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/reservas")
@Tag(name = "Reservas V2", description = "Gestión de reservas de salas con soporte HATEOAS. Estado: 1=Activa, 0=Cancelada")
public class ReservaControllerV2 {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ReservaModelAssembler assembler;

    // ─────────────────────────────────────────────
    // Consultas básicas
    // ─────────────────────────────────────────────

    @Operation(
        summary = "Obtener reserva por ID",
        description = "Retorna una reserva específica identificada por su ID numérico."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Reserva encontrada",
            content = @Content(
                mediaType = MediaTypes.HAL_JSON_VALUE,
                examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "fechaSolicitada": "2025-06-15",
                      "horaSolicitada": "09:00:00",
                      "horaCierre": "11:00:00",
                      "estado": 1,
                      "_links": {
                        "self": { "href": "/api/v2/reservas/1" },
                        "reservas": { "href": "/api/v2/reservas" }
                      }
                    }
                    """)
            )
        ),
        @ApiResponse(responseCode = "500", description = "Reserva no encontrada", content = @Content)
    })
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Reserva> getReservaById(
            @Parameter(description = "ID numérico de la reserva", example = "1", required = true)
            @PathVariable Integer id) {
        Reserva reserva = reservaService.findById(id);
        if (reserva == null) {
            throw new RuntimeException("Reserva no encontrada con ID: " + id);
        }
        return assembler.toModel(reserva);
    }

    @Operation(
        summary = "Listar todas las reservas",
        description = "Retorna el listado completo de reservas del sistema, sin filtros aplicados."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Listado obtenido exitosamente",
        content = @Content(mediaType = MediaTypes.HAL_JSON_VALUE)
    )
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Reserva>> getAllReservas() {
        List<EntityModel<Reserva>> reservas = reservaService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reservas,
                linkTo(methodOn(ReservaControllerV2.class).getAllReservas()).withSelfRel());
    }

    // ─────────────────────────────────────────────
    // Filtros por sala
    // ─────────────────────────────────────────────

    @Operation(
        summary = "Reservas por sala",
        description = "Retorna todas las reservas asociadas a una sala específica, identificada por su código."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Listado filtrado exitosamente",
            content = @Content(mediaType = MediaTypes.HAL_JSON_VALUE)),
        @ApiResponse(responseCode = "200", description = "Lista vacía si la sala no tiene reservas",
            content = @Content(mediaType = MediaTypes.HAL_JSON_VALUE,
                examples = @ExampleObject(value = """
                    { "_embedded": { "reservaList": [] }, "_links": { "self": { "href": "/api/v2/reservas/sala/1" } } }
                    """)))
    })
    @GetMapping(value = "/sala/{codigoSala}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Reserva>> getReservasBySala(
            @Parameter(description = "Código de la sala", example = "1", required = true)
            @PathVariable Integer codigoSala) {
        List<EntityModel<Reserva>> reservas = reservaService.findBySalaCodigo(codigoSala).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reservas,
                linkTo(methodOn(ReservaControllerV2.class).getReservasBySala(codigoSala)).withSelfRel());
    }

    @Operation(
        summary = "Reservas por sala y estado",
        description = "Filtra las reservas de una sala específica por su estado. Estado: 1=Activa, 0=Cancelada."
    )
    @ApiResponse(responseCode = "200", description = "Listado filtrado exitosamente",
        content = @Content(mediaType = MediaTypes.HAL_JSON_VALUE))
    @GetMapping(value = "/sala/{codigoSala}/estado/{estado}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Reserva>> getReservasSalaPorEstado(
            @Parameter(description = "Código de la sala", example = "1", required = true)
            @PathVariable Integer codigoSala,
            @Parameter(description = "Estado de la reserva: 1=Activa, 0=Cancelada", example = "1", required = true)
            @PathVariable Integer estado) {
        List<EntityModel<Reserva>> reservas = reservaService.findBySalaCodigoAndEstado(codigoSala, estado).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reservas,
                linkTo(methodOn(ReservaControllerV2.class).getReservasSalaPorEstado(codigoSala, estado)).withSelfRel());
    }

    @Operation(
        summary = "Reservas por sala en rango de fechas",
        description = "Filtra las reservas de una sala entre dos fechas (inclusive). Formato de fecha: yyyy-MM-dd."
    )
    @ApiResponse(responseCode = "200", description = "Listado filtrado exitosamente",
        content = @Content(mediaType = MediaTypes.HAL_JSON_VALUE))
    @GetMapping(value = "/sala/{codigoSala}/rango", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Reserva>> getReservasSalaPorRango(
            @Parameter(description = "Código de la sala", example = "1", required = true)
            @PathVariable Integer codigoSala,
            @Parameter(description = "Fecha de inicio del rango (yyyy-MM-dd)", example = "2025-06-01", required = true)
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
            @Parameter(description = "Fecha de fin del rango (yyyy-MM-dd)", example = "2025-06-30", required = true)
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin) {
        List<EntityModel<Reserva>> reservas = reservaService.findBySalaCodigoAndFechaSolicitadaBetween(codigoSala, inicio, fin).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reservas,
                linkTo(methodOn(ReservaControllerV2.class).getReservasSalaPorRango(codigoSala, inicio, fin)).withSelfRel());
    }

    @Operation(
        summary = "Total de reservas por sala",
        description = "Retorna el conteo total de reservas registradas para una sala específica."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Conteo obtenido",
        content = @Content(
            mediaType = MediaTypes.HAL_JSON_VALUE,
            examples = @ExampleObject(value = """
                {
                  "total": 12,
                  "_links": {
                    "self": { "href": "/api/v2/reservas/sala/1/total" },
                    "sala": { "href": "/api/v2/salas/1" }
                  }
                }
                """)
        )
    )
    @GetMapping(value = "/sala/{codigoSala}/total", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Map<String, Long>> getTotalReservasPorSala(
            @Parameter(description = "Código de la sala", example = "1", required = true)
            @PathVariable Integer codigoSala) {
        Long total = reservaService.countBySalaCodigo(codigoSala);
        Map<String, Long> respuesta = Collections.singletonMap("total", total);

        return EntityModel.of(respuesta,
                linkTo(methodOn(ReservaControllerV2.class).getTotalReservasPorSala(codigoSala)).withSelfRel(),
                linkTo(methodOn(SalaControllerV2.class).getSalaByCodigo(codigoSala)).withRel("sala"));
    }

    // ─────────────────────────────────────────────
    // Filtros por fecha
    // ─────────────────────────────────────────────

    @Operation(
        summary = "Reservas por fecha exacta",
        description = "Retorna todas las reservas solicitadas para una fecha específica. Formato: yyyy-MM-dd."
    )
    @ApiResponse(responseCode = "200", description = "Listado filtrado exitosamente",
        content = @Content(mediaType = MediaTypes.HAL_JSON_VALUE))
    @GetMapping(value = "/fecha/{fecha}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Reserva>> getReservasByFecha(
            @Parameter(description = "Fecha de la reserva en formato yyyy-MM-dd", example = "2025-06-15", required = true)
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha) {
        List<EntityModel<Reserva>> reservas = reservaService.findByFechaSolicitada(fecha).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reservas,
                linkTo(methodOn(ReservaControllerV2.class).getReservasByFecha(fecha)).withSelfRel());
    }

    @Operation(
        summary = "Reservas en rango de fechas",
        description = "Retorna todas las reservas cuya fecha solicitada se encuentra dentro del rango indicado (ambos extremos inclusive)."
    )
    @ApiResponse(responseCode = "200", description = "Listado filtrado exitosamente",
        content = @Content(mediaType = MediaTypes.HAL_JSON_VALUE))
    @GetMapping(value = "/rango", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Reserva>> getReservasPorRango(
            @Parameter(description = "Fecha de inicio del rango (yyyy-MM-dd)", example = "2025-06-01", required = true)
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
            @Parameter(description = "Fecha de fin del rango (yyyy-MM-dd)", example = "2025-06-30", required = true)
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin) {
        List<EntityModel<Reserva>> reservas = reservaService.findByFechaSolicitadaBetween(inicio, fin).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reservas,
                linkTo(methodOn(ReservaControllerV2.class).getReservasPorRango(inicio, fin)).withSelfRel());
    }

    // ─────────────────────────────────────────────
    // Filtros por estado
    // ─────────────────────────────────────────────

    @Operation(
        summary = "Reservas por estado",
        description = "Filtra todas las reservas según su estado. Estado: 1=Activa, 0=Cancelada."
    )
    @ApiResponse(responseCode = "200", description = "Listado filtrado exitosamente",
        content = @Content(mediaType = MediaTypes.HAL_JSON_VALUE))
    @GetMapping(value = "/estado/{estado}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Reserva>> getReservasByEstado(
            @Parameter(description = "Estado de la reserva: 1=Activa, 0=Cancelada", example = "1", required = true)
            @PathVariable Integer estado) {
        List<EntityModel<Reserva>> reservas = reservaService.findByEstado(estado).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reservas,
                linkTo(methodOn(ReservaControllerV2.class).getReservasByEstado(estado)).withSelfRel());
    }

    // ─────────────────────────────────────────────
    // Filtros por estudiante
    // ─────────────────────────────────────────────

    @Operation(
        summary = "Total de reservas por estudiante",
        description = "Retorna el conteo total de reservas registradas para un estudiante específico. Incluye enlace HATEOAS al recurso del estudiante."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Conteo obtenido",
        content = @Content(
            mediaType = MediaTypes.HAL_JSON_VALUE,
            examples = @ExampleObject(value = """
                {
                  "total": 5,
                  "_links": {
                    "self": { "href": "/api/v2/reservas/estudiante/1/total" },
                    "estudiante": { "href": "/api/v2/estudiantes/1" }
                  }
                }
                """)
        )
    )
    @GetMapping(value = "/estudiante/{idEstudiante}/total", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Map<String, Long>> getTotalReservasPorEstudiante(
            @Parameter(description = "ID del estudiante", example = "1", required = true)
            @PathVariable Integer idEstudiante) {
        Long total = reservaService.countByEstudianteId(idEstudiante);
        Map<String, Long> respuesta = Collections.singletonMap("total", total);

        return EntityModel.of(respuesta,
                linkTo(methodOn(ReservaControllerV2.class).getTotalReservasPorEstudiante(idEstudiante)).withSelfRel(),
                linkTo(methodOn(EstudianteControllerV2.class).getEstudianteById(idEstudiante)).withRel("estudiante"));
    }

    @Operation(
        summary = "Reservas de un estudiante en una fecha",
        description = "Filtra las reservas de un estudiante específico para una fecha exacta. Formato de fecha: yyyy-MM-dd."
    )
    @ApiResponse(responseCode = "200", description = "Listado filtrado exitosamente",
        content = @Content(mediaType = MediaTypes.HAL_JSON_VALUE))
    @GetMapping(value = "/estudiante/{idEstudiante}/fecha/{fecha}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Reserva>> getReservasEstudiantePorFecha(
            @Parameter(description = "ID del estudiante", example = "1", required = true)
            @PathVariable Integer idEstudiante,
            @Parameter(description = "Fecha exacta de la reserva (yyyy-MM-dd)", example = "2025-06-15", required = true)
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha) {
        List<EntityModel<Reserva>> reservas = reservaService.findByEstudianteIdAndFechaSolicitada(idEstudiante, fecha).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reservas,
                linkTo(methodOn(ReservaControllerV2.class).getReservasEstudiantePorFecha(idEstudiante, fecha)).withSelfRel());
    }

    @Operation(
        summary = "Reservas de un estudiante en rango de fechas",
        description = "Filtra las reservas de un estudiante entre dos fechas (ambos extremos inclusive). Formato: yyyy-MM-dd."
    )
    @ApiResponse(responseCode = "200", description = "Listado filtrado exitosamente",
        content = @Content(mediaType = MediaTypes.HAL_JSON_VALUE))
    @GetMapping(value = "/estudiante/{idEstudiante}/rango", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Reserva>> getReservasEstudiantePorRango(
            @Parameter(description = "ID del estudiante", example = "1", required = true)
            @PathVariable Integer idEstudiante,
            @Parameter(description = "Fecha de inicio del rango (yyyy-MM-dd)", example = "2025-06-01", required = true)
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
            @Parameter(description = "Fecha de fin del rango (yyyy-MM-dd)", example = "2025-06-30", required = true)
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin) {
        List<EntityModel<Reserva>> reservas = reservaService.findByEstudianteIdAndFechaSolicitadaBetween(idEstudiante, inicio, fin).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reservas,
                linkTo(methodOn(ReservaControllerV2.class).getReservasEstudiantePorRango(idEstudiante, inicio, fin)).withSelfRel());
    }
}
