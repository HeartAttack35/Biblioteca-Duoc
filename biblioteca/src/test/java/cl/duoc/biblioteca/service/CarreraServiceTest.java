package cl.duoc.biblioteca.service;

import cl.duoc.biblioteca.model.Carrera;
import cl.duoc.biblioteca.repository.CarreraRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CarreraServiceTest {

    @Autowired
    private CarreraService carreraService;

    @MockitoBean
    private CarreraRepository carreraRepository;

    @Test
    @DisplayName("Debe retornar una lista con todas las carreras")
    public void testFindAll() {
        Carrera carrera = new Carrera();
        carrera.setCodigo("INF101");
        carrera.setNombre("Ingeniería Informática");
        when(carreraRepository.findAll()).thenReturn(List.of(carrera));

        List<Carrera> carreras = carreraService.findAll();

        assertNotNull(carreras);
        assertEquals(1, carreras.size());
        assertEquals("Ingeniería Informática", carreras.get(0).getNombre());
    }

    @Test
    @DisplayName("Debe guardar y retornar una nueva carrera")
    public void testSave() {
        Carrera nuevaCarrera = new Carrera();
        nuevaCarrera.setCodigo("DIS202");
        nuevaCarrera.setNombre("Diseño Gráfico");
        when(carreraRepository.save(any(Carrera.class))).thenReturn(nuevaCarrera);

        Carrera guardada = carreraService.save(nuevaCarrera);

        assertNotNull(guardada);
        assertEquals("DIS202", guardada.getCodigo());
    }
}