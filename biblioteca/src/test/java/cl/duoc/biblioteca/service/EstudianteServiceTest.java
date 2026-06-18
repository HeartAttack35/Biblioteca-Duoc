package cl.duoc.biblioteca.service;

import cl.duoc.biblioteca.model.Estudiante;
import cl.duoc.biblioteca.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstudianteServiceTest {
    @Autowired
    private EstudianteRepository estudianteRepository;

    public List<Estudiante> findAll() {
        return estudianteRepository.findAll();
    }

    public Estudiante findById(Integer id) {
        return estudianteRepository.findById(id).orElse(null);
    }

    public Estudiante save(Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }

    public void deleteById(Integer id) {
        estudianteRepository.deleteById(id);
    }
}