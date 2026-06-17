package cl.duoc.biblioteca.repository;

import cl.duoc.biblioteca.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    List<Reserva> findByEstudianteId(Integer idEstudiante);

    List<Reserva> findBySalaCodigo(Integer codigoSala);

    List<Reserva> findByFechaSolicitada(Date fecha);

    List<Reserva> findByEstado(Integer estado);

    List<Reserva> findByFechaSolicitadaBetween(Date start, Date end);

    Long countByEstudianteId(Integer idEstudiante);
}