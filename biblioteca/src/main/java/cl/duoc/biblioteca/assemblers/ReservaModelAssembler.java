package cl.duoc.biblioteca.assemblers;

import cl.duoc.biblioteca.controller.ReservaControllerV2;
import cl.duoc.biblioteca.model.Reserva;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ReservaModelAssembler implements RepresentationModelAssembler<Reserva, EntityModel<Reserva>> {

    @Override
    public EntityModel<Reserva> toModel(Reserva reserva) {
        return EntityModel.of(reserva,
                // Enlace al recurso individual (self)
                linkTo(methodOn(ReservaControllerV2.class).getReservaById(reserva.getId())).withSelfRel(),
                // Enlace a la colección completa (reservas)
                linkTo(methodOn(ReservaControllerV2.class).getAllReservas()).withRel("reservas"));
    }
}