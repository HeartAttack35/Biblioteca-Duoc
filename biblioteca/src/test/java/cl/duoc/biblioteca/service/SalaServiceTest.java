package cl.duoc.biblioteca.service;

import cl.duoc.biblioteca.model.Sala;
import cl.duoc.biblioteca.repository.SalaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SalaServiceTest {

    @Autowired
    private SalaService salaService;

    @MockitoBean
    private SalaRepository salaRepository;

@Test
    @DisplayName("Debe encontrar una sala por su código")
    public void testFindById() {
        Sala salaMock = new Sala();
        salaMock.setCodigo(1);
        salaMock.setNombre("Sala de Estudio A");
        salaMock.setCapacidad(10);
        
        when(salaRepository.findById(1)).thenReturn(Optional.of(salaMock));

        Sala resultado = salaService.findById(1);

        assertNotNull(resultado);
        assertEquals("Sala de Estudio A", resultado.getNombre());
        assertEquals(10, resultado.getCapacidad());
    }

    @Test
    @DisplayName("Debe retornar null si la sala no existe")
    public void testFindById_NotFound() {
        when(salaRepository.findById(99)).thenReturn(Optional.empty());

        Sala resultado = salaService.findById(99);

        assertNull(resultado);
    }
}