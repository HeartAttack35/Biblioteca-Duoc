package cl.duoc.biblioteca.service;

import cl.duoc.biblioteca.model.Reserva;
import cl.duoc.biblioteca.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }

    public Reserva findById(Integer id) {
        return reservaRepository.findById(id).orElse(null);
    }

    public Reserva save(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    public List<Reserva> findByEstudianteId(Integer idEstudiante) {
        return reservaRepository.findByEstudianteId(idEstudiante);
    }

    public List<Reserva> findBySalaCodigo(Integer codigoSala) {
        return reservaRepository.findBySalaCodigo(codigoSala);
    }

    public List<Reserva> findByFechaSolicitada(Date fecha) {
        return reservaRepository.findByFechaSolicitada(fecha);
    }

    public List<Reserva> findByEstado(Integer estado) {
        return reservaRepository.findByEstado(estado);
    }

    public List<Reserva> findByFechaSolicitadaBetween(Date start, Date end) {
        return reservaRepository.findByFechaSolicitadaBetween(start, end);
    }

    public Long countByEstudianteId(Integer idEstudiante) {
        return reservaRepository.countByEstudianteId(idEstudiante);
    }

    public List<Reserva> findByEstudianteIdAndFechaSolicitada(Integer idEstudiante, Date fecha) {
        return reservaRepository.findByEstudianteIdAndFechaSolicitada(idEstudiante, fecha);
    }

    public List<Reserva> findBySalaCodigoAndEstado(Integer codigoSala, Integer estado) {
        return reservaRepository.findBySalaCodigoAndEstado(codigoSala, estado);
    }

    public List<Reserva> findByEstudianteIdAndFechaSolicitadaBetween(Integer idEstudiante, Date start, Date end) {
        return reservaRepository.findByEstudianteIdAndFechaSolicitadaBetween(idEstudiante, start, end);
    }

    public List<Reserva> findBySalaCodigoAndFechaSolicitadaBetween(Integer codigoSala, Date start, Date end) {
        return reservaRepository.findBySalaCodigoAndFechaSolicitadaBetween(codigoSala, start, end);
    }

    public Long countBySalaCodigo(Integer codigoSala) {
        return reservaRepository.countBySalaCodigo(codigoSala);
    }
}