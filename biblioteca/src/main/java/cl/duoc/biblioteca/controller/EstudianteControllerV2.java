package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.assemblers.EstudianteModelAssembler;
import cl.duoc.biblioteca.model.Estudiante;
import cl.duoc.biblioteca.service.EstudianteService;
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
public class EstudianteControllerV2 {

    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private EstudianteModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Estudiante>> getAllEstudiantes() {
        List<EntityModel<Estudiante>> estudiantes = estudianteService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(estudiantes,
                linkTo(methodOn(EstudianteControllerV2.class).getAllEstudiantes()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Estudiante> getEstudianteById(@PathVariable Integer id) {
        Estudiante estudiante = estudianteService.findById(id);
        if (estudiante == null) {
            throw new RuntimeException("Estudiante no encontrado con ID: " + id);
        }
        return assembler.toModel(estudiante);
    }
}