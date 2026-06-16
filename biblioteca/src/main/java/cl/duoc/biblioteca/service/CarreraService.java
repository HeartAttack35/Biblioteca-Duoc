package cl.duoc.biblioteca.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.biblioteca.repository.CarreraRepository;
import cl.duoc.biblioteca.model.Carrera;
import java.util.List;

@Service
public class CarreraService {

    @Autowired
    private CarreraRepository carreraRepository;
   
    public List<Carrera> findAll() {
        return carreraRepository.findAll();
    }

    public Carrera save(Carrera carrera) {
        return carreraRepository.save(carrera);
    }

    public Carrera findById(String codigo) {
        return carreraRepository.findById(codigo).orElse(null);
    }

    public void delete(String codigo) {
        carreraRepository.deleteById(codigo);
    }
}
