package cl.duoc.biblioteca.assemblers;

import cl.duoc.biblioteca.controller.TipoSalaControllerV2;
import cl.duoc.biblioteca.model.TipoSala;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class TipoSalaModelAssembler implements RepresentationModelAssembler<TipoSala, EntityModel<TipoSala>> {

    @Override
    public EntityModel<TipoSala> toModel(TipoSala tipoSala) {
        return EntityModel.of(tipoSala,
                linkTo(methodOn(TipoSalaControllerV2.class).getTipoSalaById(tipoSala.getIdTipo())).withSelfRel(),
                linkTo(methodOn(TipoSalaControllerV2.class).getAllTipoSalas()).withRel("tiposSalas"));
    }
}