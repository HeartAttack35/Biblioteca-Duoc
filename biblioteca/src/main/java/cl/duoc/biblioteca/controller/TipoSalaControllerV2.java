package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.assemblers.TipoSalaModelAssembler;
import cl.duoc.biblioteca.model.TipoSala;
import cl.duoc.biblioteca.service.TipoSalaService;
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
public class TipoSalaControllerV2 {

    @Autowired
    private TipoSalaService tipoSalaService;

    @Autowired
    private TipoSalaModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<TipoSala>> getAllTipoSalas() {
        List<EntityModel<TipoSala>> tipos = tipoSalaService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(tipos,
                linkTo(methodOn(TipoSalaControllerV2.class).getAllTipoSalas()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<TipoSala> getTipoSalaById(@PathVariable Integer id) {
        TipoSala tipo = tipoSalaService.findById(id);
        if (tipo == null) {
            throw new RuntimeException("Tipo de sala no encontrado con ID: " + id);
        }
        return assembler.toModel(tipo);
    }
}