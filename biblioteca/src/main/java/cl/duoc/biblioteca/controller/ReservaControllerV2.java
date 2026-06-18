package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.assemblers.ReservaModelAssembler;
import cl.duoc.biblioteca.model.Reserva;
import cl.duoc.biblioteca.service.ReservaService;
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
public class ReservaControllerV2 {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ReservaModelAssembler assembler;

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Reserva> getReservaById(@PathVariable Integer id) {
        Reserva reserva = reservaService.findById(id);
        if (reserva == null) {
            throw new RuntimeException("Reserva no encontrada con ID: " + id);
        }
        return assembler.toModel(reserva);
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Reserva>> getAllReservas() {
        List<EntityModel<Reserva>> reservas = reservaService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reservas,
                linkTo(methodOn(ReservaControllerV2.class).getAllReservas()).withSelfRel());
    }

    
    @GetMapping(value = "/sala/{codigoSala}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Reserva>> getReservasBySala(@PathVariable Integer codigoSala) {
        List<EntityModel<Reserva>> reservas = reservaService.findBySalaCodigo(codigoSala).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reservas,
                linkTo(methodOn(ReservaControllerV2.class).getReservasBySala(codigoSala)).withSelfRel());
    }

    // 2. Obtener todas las reservas en una fecha específica
    @GetMapping(value = "/fecha/{fecha}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Reserva>> getReservasByFecha(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha) {
        List<EntityModel<Reserva>> reservas = reservaService.findByFechaSolicitada(fecha).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reservas,
                linkTo(methodOn(ReservaControllerV2.class).getReservasByFecha(fecha)).withSelfRel());
    }

    // 3. Obtener todas las reservas con un estado específico
    @GetMapping(value = "/estado/{estado}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Reserva>> getReservasByEstado(@PathVariable Integer estado) {
        List<EntityModel<Reserva>> reservas = reservaService.findByEstado(estado).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reservas,
                linkTo(methodOn(ReservaControllerV2.class).getReservasByEstado(estado)).withSelfRel());
    }

    // 4. Obtener todas las reservas entre dos fechas
    @GetMapping(value = "/rango", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Reserva>> getReservasPorRango(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin) {
        List<EntityModel<Reserva>> reservas = reservaService.findByFechaSolicitadaBetween(inicio, fin).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reservas,
                linkTo(methodOn(ReservaControllerV2.class).getReservasPorRango(inicio, fin)).withSelfRel());
    }

    @GetMapping(value = "/estudiante/{idEstudiante}/total", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Map<String, Long>> getTotalReservasPorEstudiante(@PathVariable Integer idEstudiante) {
        Long total = reservaService.countByEstudianteId(idEstudiante);
        
        Map<String, Long> respuesta = Collections.singletonMap("total", total);
        
        return EntityModel.of(respuesta,
                linkTo(methodOn(ReservaControllerV2.class).getTotalReservasPorEstudiante(idEstudiante)).withSelfRel(),
                linkTo(methodOn(EstudianteControllerV2.class).getEstudianteById(idEstudiante)).withRel("estudiante"));
    }
    
    @GetMapping(value = "/estudiante/{idEstudiante}/fecha/{fecha}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Reserva>> getReservasEstudiantePorFecha(
            @PathVariable Integer idEstudiante,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha) {
        List<EntityModel<Reserva>> reservas = reservaService.findByEstudianteIdAndFechaSolicitada(idEstudiante, fecha).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reservas,
                linkTo(methodOn(ReservaControllerV2.class).getReservasEstudiantePorFecha(idEstudiante, fecha)).withSelfRel());
    }

    @GetMapping(value = "/sala/{codigoSala}/estado/{estado}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Reserva>> getReservasSalaPorEstado(
            @PathVariable Integer codigoSala,
            @PathVariable Integer estado) {
        List<EntityModel<Reserva>> reservas = reservaService.findBySalaCodigoAndEstado(codigoSala, estado).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reservas,
                linkTo(methodOn(ReservaControllerV2.class).getReservasSalaPorEstado(codigoSala, estado)).withSelfRel());
    }

    @GetMapping(value = "/estudiante/{idEstudiante}/rango", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Reserva>> getReservasEstudiantePorRango(
            @PathVariable Integer idEstudiante,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin) {
        List<EntityModel<Reserva>> reservas = reservaService.findByEstudianteIdAndFechaSolicitadaBetween(idEstudiante, inicio, fin).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reservas,
                linkTo(methodOn(ReservaControllerV2.class).getReservasEstudiantePorRango(idEstudiante, inicio, fin)).withSelfRel());
    }
    
    @GetMapping(value = "/sala/{codigoSala}/rango", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Reserva>> getReservasSalaPorRango(
            @PathVariable Integer codigoSala,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin) {
        List<EntityModel<Reserva>> reservas = reservaService.findBySalaCodigoAndFechaSolicitadaBetween(codigoSala, inicio, fin).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reservas,
                linkTo(methodOn(ReservaControllerV2.class).getReservasSalaPorRango(codigoSala, inicio, fin)).withSelfRel());
    }

    @GetMapping(value = "/sala/{codigoSala}/total", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Map<String, Long>> getTotalReservasPorSala(@PathVariable Integer codigoSala) {
        Long total = reservaService.countBySalaCodigo(codigoSala);
        Map<String, Long> respuesta = Collections.singletonMap("total", total);

        return EntityModel.of(respuesta,
                linkTo(methodOn(ReservaControllerV2.class).getTotalReservasPorSala(codigoSala)).withSelfRel(),
                linkTo(methodOn(SalaControllerV2.class).getSalaByCodigo(codigoSala)).withRel("sala"));
    }
}