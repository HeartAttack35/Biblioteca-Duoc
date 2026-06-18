package cl.duoc.biblioteca.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import cl.duoc.biblioteca.model.Carrera;
import cl.duoc.biblioteca.repository.CarreraRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

public class CarreraServiceTest {

    @Mock
    private CarreraRepository carreraRepository;

    @InjectMocks
    private CarreraService carreraService;

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
        Carrera carreraMock = new Carrera();
        carreraMock.setCodigo("1");
        carreraMock.setNombre("Ingeniería");
        when(carreraRepository.findAll()).thenReturn(List.of(carreraMock));

        List<Carrera> carreras = carreraService.findAll();

        assertNotNull(carreras);
        assertEquals(1, carreras.size());
        assertEquals("1", carreras.get(0).getCodigo());
    }

    @Test
    public void testFindByCodigo() {
        String codigo = "1";
        Carrera carrera = new Carrera();
        carrera.setCodigo(codigo);
        carrera.setNombre("Ingeniería");
        when(carreraRepository.findById(codigo)).thenReturn(Optional.of(carrera));

        Carrera found = carreraService.findById(codigo);

        assertNotNull(found);
        assertEquals(codigo, found.getCodigo());
    }

    @Test
    public void testSave() {
        Carrera carrera = new Carrera();
        carrera.setCodigo("1");
        carrera.setNombre("Ingeniería");
        when(carreraRepository.save(carrera)).thenReturn(carrera);

        Carrera saved = carreraService.save(carrera);

        assertNotNull(saved);
        assertEquals("Ingeniería", saved.getNombre());
    }

    @Test
    public void testDeleteByCodigo() {
        String codigo = "1";
        doNothing().when(carreraRepository).deleteById(codigo);

        carreraService.delete(codigo);

        verify(carreraRepository, times(1)).deleteById(codigo);
    }
}
