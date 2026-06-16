package cl.duoc.biblioteca.assemblers;

import cl.duoc.biblioteca.controller.CarreraControllerV2;
import cl.duoc.biblioteca.model.Carrera;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CarreraModelAssembler implements RepresentationModelAssembler<Carrera, EntityModel<Carrera>> {

    @Override
    public EntityModel<Carrera> toModel(Carrera carrera) {
        return EntityModel.of(carrera,
            linkTo(methodOn(CarreraControllerV2.class).getCarreraByCodigo(carrera.getCodigo())).withSelfRel(),
            linkTo(methodOn(CarreraControllerV2.class).getAllCarreras()).withRel("carreras")
        );
    }
}
