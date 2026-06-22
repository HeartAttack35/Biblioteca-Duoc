package cl.duoc.biblioteca.ms_autor.service;

import cl.duoc.biblioteca.ms_autor.model.Autor;
import cl.duoc.biblioteca.ms_autor.repository.AutorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutorServiceTest {

    @Mock
    private AutorRepository autorRepository;

    @InjectMocks
    private AutorService autorService;

    private Autor autor1;
    private Autor autor2;

    @BeforeEach
    void setUp() {
        autor1 = new Autor("Isabel Allende");
        autor1.setId(1);

        autor2 = new Autor("Mario Vargas Llosa");
        autor2.setId(2);
    }

    @Test
    void listarTodos_debeRetornarListaDeAutores() {
        when(autorRepository.findAll()).thenReturn(List.of(autor1, autor2));

        List<Autor> resultado = autorService.listarTodos();

        assertEquals(2, resultado.size());
        assertEquals("Isabel Allende", resultado.get(0).getNombre());
        verify(autorRepository, times(1)).findAll();
    }

    @Test
    void listarTodos_conRepositorioVacio_debeRetornarListaVacia() {
        when(autorRepository.findAll()).thenReturn(List.of());

        List<Autor> resultado = autorService.listarTodos();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarPorId_conIdExistente_debeRetornarAutor() {
        when(autorRepository.findById(1)).thenReturn(Optional.of(autor1));

        Optional<Autor> resultado = autorService.buscarPorId(1);

        assertTrue(resultado.isPresent());
        assertEquals("Isabel Allende", resultado.get().getNombre());
    }

    @Test
    void buscarPorId_conIdInexistente_debeRetornarVacio() {
        when(autorRepository.findById(99)).thenReturn(Optional.empty());

        Optional<Autor> resultado = autorService.buscarPorId(99);

        assertFalse(resultado.isPresent());
    }

    @Test
    void crearAutor_debeGuardarYRetornarAutor() {
        Autor nuevoAutor = new Autor("Jorge Luis Borges");
        Autor autorGuardado = new Autor("Jorge Luis Borges");
        autorGuardado.setId(3);

        when(autorRepository.save(nuevoAutor)).thenReturn(autorGuardado);

        Autor resultado = autorService.crearAutor(nuevoAutor);

        assertNotNull(resultado.getId());
        assertEquals("Jorge Luis Borges", resultado.getNombre());
        verify(autorRepository, times(1)).save(nuevoAutor);
    }
}
