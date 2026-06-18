package cl.duoc.biblioteca.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cl.duoc.biblioteca.assemblers.SalaModelAssembler;
import cl.duoc.biblioteca.model.Sala;
import cl.duoc.biblioteca.service.SalaService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;

public class SalaControllerTest {

    @Mock
    private SalaService salaService;

    @Mock
    private SalaModelAssembler assembler;

    @InjectMocks
    private SalaControllerV2 salaController;

    private AutoCloseable closeable;
    private Sala sala;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        sala = new Sala();
        sala.setCodigo(1);
        sala.setNombre("Sala 101");
        sala.setCapacidad(50);
        sala.setIdInstituto(1);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testGetAllSalas() {
        when(salaService.findAll()).thenReturn(List.of(sala));
        when(assembler.toModel(sala)).thenReturn(EntityModel.of(sala));

        CollectionModel<EntityModel<Sala>> result = salaController.getAllSalas();

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(sala, result.getContent().iterator().next().getContent());
    }

    @Test
    public void testGetSalaById() {
        when(salaService.findById(1)).thenReturn(sala);
        when(assembler.toModel(sala)).thenReturn(EntityModel.of(sala));

        EntityModel<Sala> result = salaController.getSalaByCodigo(1);

        assertNotNull(result);
        assertEquals(1, result.getContent().getCodigo());
        assertEquals("Sala 101", result.getContent().getNombre());
    }
}
