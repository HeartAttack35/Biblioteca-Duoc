package cl.duoc.biblioteca.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estudiante", uniqueConstraints = {
    @UniqueConstraint(name = "estudiante_UN", columnNames = {"run"})
})
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "run", nullable = false, length = 9)
    private String run;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "correo", length = 100)
    private String correo;

    @Column(name = "jornada", length = 1)
    private String jornada;

    private Integer telefono;

    // Relación Muchos a Uno con Carrera (codigo_carrera)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_carrera", referencedColumnName = "codigo")
    private Carrera carrera;

    // Constructor vacío requerido por JPA
    public Estudiante() {
    }

    // Constructor con parámetros
    public Estudiante(String run, String nombres, String correo, String jornada, Integer telefono, Carrera carrera) {
        this.run = run;
        this.nombres = nombres;
        this.correo = correo;
        this.jornada = jornada;
        this.telefono = telefono;
        this.carrera = carrera;
    }

    // --- Getters y Setters ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRun() {
        return run;
    }

    public void setRun(String run) {
        this.run = run;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getJornada() {
        return jornada;
    }

    public void setJornada(String jornada) {
        this.jornada = jornada;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }
}