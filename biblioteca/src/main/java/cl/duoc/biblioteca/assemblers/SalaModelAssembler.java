package cl.duoc.biblioteca.assemblers;

import cl.duoc.biblioteca.controller.SalaControllerV2;
import cl.duoc.biblioteca.model.Sala;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class SalaModelAssembler implements RepresentationModelAssembler<Sala, EntityModel<Sala>> {

    @Override
    public EntityModel<Sala> toModel(Sala sala) {
        return EntityModel.of(sala,
                linkTo(methodOn(SalaControllerV2.class).getSalaByCodigo(sala.getCodigo())).withSelfRel(),
                linkTo(methodOn(SalaControllerV2.class).getAllSalas()).withRel("salas"));
    }
}