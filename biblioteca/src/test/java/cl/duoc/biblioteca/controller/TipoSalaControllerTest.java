package cl.duoc.biblioteca.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cl.duoc.biblioteca.assemblers.TipoSalaModelAssembler;
import cl.duoc.biblioteca.model.TipoSala;
import cl.duoc.biblioteca.service.TipoSalaService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;

public class TipoSalaControllerTest {

    @Mock
    private TipoSalaService tipoSalaService;

    @Mock
    private TipoSalaModelAssembler assembler;

    @InjectMocks
    private TipoSalaControllerV2 tipoSalaController;

    private AutoCloseable closeable;
    private TipoSala tipoSala;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        tipoSala = new TipoSala();
        tipoSala.setIdTipo(1);
        tipoSala.setNombre("Laboratorio");
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testGetAllTipoSalas() {
        when(tipoSalaService.findAll()).thenReturn(List.of(tipoSala));
        when(assembler.toModel(tipoSala)).thenReturn(EntityModel.of(tipoSala));

        CollectionModel<EntityModel<TipoSala>> result = tipoSalaController.getAllTipoSalas();

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(tipoSala, result.getContent().iterator().next().getContent());
    }

    @Test
    public void testGetTipoSalaById() {
        when(tipoSalaService.findById(1)).thenReturn(tipoSala);
        when(assembler.toModel(tipoSala)).thenReturn(EntityModel.of(tipoSala));

        EntityModel<TipoSala> result = tipoSalaController.getTipoSalaById(1);

        assertNotNull(result);
        assertEquals(1, result.getContent().getIdTipo());
        assertEquals("Laboratorio", result.getContent().getNombre());
    }
}
