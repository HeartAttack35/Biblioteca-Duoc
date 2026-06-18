package cl.duoc.biblioteca.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import cl.duoc.biblioteca.model.Sala;
import cl.duoc.biblioteca.model.TipoSala;
import cl.duoc.biblioteca.repository.SalaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

public class SalaServiceTest {

    @Mock
    private SalaRepository salaRepository;

    @InjectMocks
    private SalaService salaService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testFindAll() {
        TipoSala tipo = new TipoSala();
        tipo.setNombre("Laboratorio");
        Sala s = new Sala("Sala A", 30, 1, tipo);
        s.setCodigo(1);
        when(salaRepository.findAll()).thenReturn(List.of(s));

        List<Sala> salas = salaService.findAll();
        assertNotNull(salas);
        assertEquals(1, salas.size());
        assertEquals(s, salas.get(0));
    }

    @Test
    public void testFindById() {
        Integer id = 1;
        TipoSala tipo = new TipoSala();
        tipo.setNombre("Laboratorio");
        Sala sala = new Sala("Sala A", 30, 1, tipo);
        sala.setCodigo(id);
        when(salaRepository.findById(id)).thenReturn(Optional.of(sala));

        Sala found = salaService.findById(id);
        assertNotNull(found);
        assertEquals(id, found.getCodigo());
    }

    @Test
    public void testSave() {
        TipoSala tipo = new TipoSala();
        tipo.setNombre("Laboratorio");
        Sala sala = new Sala("Sala A", 30, 1, tipo);
        sala.setCodigo(1);
        when(salaRepository.save(sala)).thenReturn(sala);

        Sala saved = salaService.save(sala);
        assertNotNull(saved);
        assertEquals("Sala A", saved.getNombre());
    }

    @Test
    public void testDeleteById() {
        Integer id = 1;
        doNothing().when(salaRepository).deleteById(id);

        salaService.deleteById(id);
        verify(salaRepository, times(1)).deleteById(id);
    }
}
