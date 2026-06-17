package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.assemblers.SalaModelAssembler;
import cl.duoc.biblioteca.model.Sala;
import cl.duoc.biblioteca.service.SalaService;
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
public class SalaControllerV2 {

    @Autowired
    private SalaService salaService;

    @Autowired
    private SalaModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Sala>> getAllSalas() {
        List<EntityModel<Sala>> salas = salaService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(salas,
                linkTo(methodOn(SalaControllerV2.class).getAllSalas()).withSelfRel());
    }

    @GetMapping(value = "/{codigo}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Sala> getSalaByCodigo(@PathVariable Integer codigo) {
        Sala sala = salaService.findById(codigo);
        if (sala == null) {
            throw new RuntimeException("Sala no encontrada con código: " + codigo);
        }
        return assembler.toModel(sala);
    }
}