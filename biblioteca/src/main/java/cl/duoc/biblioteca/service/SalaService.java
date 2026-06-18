package cl.duoc.biblioteca.service;

import cl.duoc.biblioteca.model.Sala;
import cl.duoc.biblioteca.repository.SalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaService {

    @Autowired
    private SalaRepository salaRepository;

    public List<Sala> findAll() {
        return salaRepository.findAll();
    }

    public Sala findById(Integer codigo) {
        return salaRepository.findById(codigo).orElse(null);
    }

    public Sala save(Sala sala) {
        return salaRepository.save(sala);
    }

    public void deleteById(Integer id) {
        salaRepository.deleteById(id);
    }
}