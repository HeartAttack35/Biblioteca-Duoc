package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.model.Carrera;
import cl.duoc.biblioteca.service.CarreraService;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebMvcTest(CarreraController.class)
public class CarreraControllerTest {

    @Autowired
    @MockitoBean
    private CarreraService carreraService;

    @GetMapping
    public List<Carrera> getAllCarreras() {
        return carreraService.findAll();
    }

    @GetMapping("/{codigo}")
    public Carrera getCarreraByCodigo(@PathVariable String codigo) {
        return carreraService.findById(codigo);
    }

    @PostMapping
    public Carrera createCarrera(@RequestBody Carrera carrera) {
        return carreraService.save(carrera);
    }

    @PutMapping("/{codigo}")
    public Carrera updateCarrera(@PathVariable String codigo, @RequestBody Carrera carrera) {
        carrera.setCodigo(codigo);
        return carreraService.save(carrera);
    }

    @DeleteMapping("/{codigo}")
    public void deleteCarrera(@PathVariable String codigo) {
        carreraService.delete(codigo);
    }
}
