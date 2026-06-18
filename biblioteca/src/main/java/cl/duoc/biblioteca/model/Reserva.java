package cl.duoc.biblioteca.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estudiante", referencedColumnName = "id")
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_sala", referencedColumnName = "codigo")
    private Sala sala;

    @Column(name = "fecha_solicitada")
    @Temporal(TemporalType.DATE)
    private Date fechaSolicitada;

    @Column(name = "hora_solicitada")
    @Temporal(TemporalType.TIME)
    private Date horaSolicitada;

    @Column(name = "hora_cierre")
    @Temporal(TemporalType.TIME)
    private Date horaCierre;

    @Column(name = "estado")
    private Integer estado;

    public Reserva() {
    }

    public Reserva(Estudiante estudiante, Sala sala, Date fechaSolicitada, Date horaSolicitada, Date horaCierre, Integer estado) {
        this.estudiante = estudiante;
        this.sala = sala;
        this.fechaSolicitada = fechaSolicitada;
        this.horaSolicitada = horaSolicitada;
        this.horaCierre = horaCierre;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public Date getFechaSolicitada() {
        return fechaSolicitada;
    }

    public void setFechaSolicitada(Date fechaSolicitada) {
        this.fechaSolicitada = fechaSolicitada;
    }

    public Date getHoraSolicitada() {
        return horaSolicitada;
    }

    public void setHoraSolicitada(Date horaSolicitada) {
        this.horaSolicitada = horaSolicitada;
    }

    public Date getHoraCierre() {
        return horaCierre;
    }

    public void setHoraCierre(Date horaCierre) {
        this.horaCierre = horaCierre;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}