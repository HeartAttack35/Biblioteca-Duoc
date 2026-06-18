package cl.duoc.biblioteca.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import cl.duoc.biblioteca.model.TipoSala;
import cl.duoc.biblioteca.repository.TipoSalaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

public class TipoSalaServiceTest {

    @Mock
    private TipoSalaRepository tipoSalaRepository;

    @InjectMocks
    private TipoSalaService tipoSalaService;

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
        tipo.setIdTipo(1);
        tipo.setNombre("Laboratorio");
        when(tipoSalaRepository.findAll()).thenReturn(List.of(tipo));

        List<TipoSala> tipos = tipoSalaService.findAll();
        assertNotNull(tipos);
        assertEquals(1, tipos.size());
        assertEquals(tipo, tipos.get(0));
    }

    @Test
    public void testFindById() {
        Integer id = 1;
        TipoSala tipoSala = new TipoSala();
        tipoSala.setIdTipo(id);
        tipoSala.setNombre("Laboratorio");
        when(tipoSalaRepository.findById(id)).thenReturn(Optional.of(tipoSala));

        TipoSala found = tipoSalaService.findById(id);
        assertNotNull(found);
        assertEquals(id, found.getIdTipo());
    }

    @Test
    public void testSave() {
        TipoSala tipoSala = new TipoSala();
        tipoSala.setIdTipo(1);
        tipoSala.setNombre("Laboratorio");
        when(tipoSalaRepository.save(tipoSala)).thenReturn(tipoSala);

        TipoSala saved = tipoSalaService.save(tipoSala);
        assertNotNull(saved);
        assertEquals("Laboratorio", saved.getNombre());
    }

    @Test
    public void testDeleteById() {
        Integer id = 1;
        doNothing().when(tipoSalaRepository).deleteById(id);

        tipoSalaService.deleteById(id);
        verify(tipoSalaRepository, times(1)).deleteById(id);
    }
}
