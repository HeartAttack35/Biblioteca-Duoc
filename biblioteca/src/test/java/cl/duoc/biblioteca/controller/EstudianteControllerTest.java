package cl.duoc.biblioteca.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cl.duoc.biblioteca.assemblers.EstudianteModelAssembler;
import cl.duoc.biblioteca.model.Estudiante;
import cl.duoc.biblioteca.service.EstudianteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;

public class EstudianteControllerTest {

    @Mock
    private EstudianteService estudianteService;

    @Mock
    private EstudianteModelAssembler assembler;

    @InjectMocks
    private EstudianteControllerV2 estudianteController;

    private AutoCloseable closeable;
    private Estudiante estudiante;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        estudiante = new Estudiante();
        estudiante.setId(1);
        estudiante.setRun("12345678-9");
        estudiante.setNombres("Juan Pérez");
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testGetAllEstudiantes() {
        when(estudianteService.findAll()).thenReturn(List.of(estudiante));
        when(assembler.toModel(estudiante)).thenReturn(EntityModel.of(estudiante));

        CollectionModel<EntityModel<Estudiante>> result = estudianteController.getAllEstudiantes();

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(estudiante, result.getContent().iterator().next().getContent());
    }

    @Test
    public void testGetEstudianteById() {
        when(estudianteService.findById(1)).thenReturn(estudiante);
        when(assembler.toModel(estudiante)).thenReturn(EntityModel.of(estudiante));

        EntityModel<Estudiante> result = estudianteController.getEstudianteById(1);

        assertNotNull(result);
        assertEquals(1, result.getContent().getId());
        assertEquals("12345678-9", result.getContent().getRun());
        assertEquals("Juan Pérez", result.getContent().getNombres());
    }
}
