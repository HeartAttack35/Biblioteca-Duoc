package cl.duoc.biblioteca.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cl.duoc.biblioteca.assemblers.ReservaModelAssembler;
import cl.duoc.biblioteca.model.Reserva;
import cl.duoc.biblioteca.service.ReservaService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.Date;
import java.util.List;

public class ReservaControllerTest {

    @Mock
    private ReservaService reservaService;

    @Mock
    private ReservaModelAssembler assembler;

    @InjectMocks
    private ReservaControllerV2 reservaController;

    private AutoCloseable closeable;
    private Reserva reserva;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        reserva = new Reserva();
        reserva.setId(1);
        reserva.setFechaSolicitada(new Date());
        reserva.setHoraSolicitada(new Date());
        reserva.setHoraCierre(new Date(System.currentTimeMillis() + 3600000));
        reserva.setEstado(1);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testGetAllReservas() {
        when(reservaService.findAll()).thenReturn(List.of(reserva));
        when(assembler.toModel(reserva)).thenReturn(EntityModel.of(reserva));

        CollectionModel<EntityModel<Reserva>> result = reservaController.getAllReservas();

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(reserva, result.getContent().iterator().next().getContent());
    }

    @Test
    public void testGetReservaById() {
        when(reservaService.findById(1)).thenReturn(reserva);
        when(assembler.toModel(reserva)).thenReturn(EntityModel.of(reserva));

        EntityModel<Reserva> result = reservaController.getReservaById(1);

        assertNotNull(result);
        assertEquals(1, result.getContent().getId());
        assertEquals(1, result.getContent().getEstado());
    }
}
