package cl.duoc.biblioteca.assemblers;

import cl.duoc.biblioteca.controller.EstudianteControllerV2;
import cl.duoc.biblioteca.model.Estudiante;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class EstudianteModelAssembler implements RepresentationModelAssembler<Estudiante, EntityModel<Estudiante>> {

    @Override
    public EntityModel<Estudiante> toModel(Estudiante estudiante) {
        return EntityModel.of(estudiante,
                linkTo(methodOn(EstudianteControllerV2.class).getEstudianteById(estudiante.getId())).withSelfRel(),
                linkTo(methodOn(EstudianteControllerV2.class).getAllEstudiantes()).withRel("estudiantes"));
    }
}