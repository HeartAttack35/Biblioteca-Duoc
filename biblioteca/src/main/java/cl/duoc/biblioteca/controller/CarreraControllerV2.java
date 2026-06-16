package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.assemblers.CarreraModelAssembler;
import cl.duoc.biblioteca.model.Carrera;
import cl.duoc.biblioteca.service.CarreraService;
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
public class CarreraControllerV2 {

    @Autowired
    private CarreraService carreraService;

    @Autowired
    private CarreraModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Carrera>> getAllCarreras() {
        List<EntityModel<Carrera>> carreras = carreraService.findAll().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(carreras,
            linkTo(methodOn(CarreraControllerV2.class).getAllCarreras()).withSelfRel()
        );
    }

    @GetMapping(value = "/{codigo}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Carrera> getCarreraByCodigo(@PathVariable String codigo) {
        Carrera carrera = carreraService.findById(codigo);
        return assembler.toModel(carrera);
    }
}
