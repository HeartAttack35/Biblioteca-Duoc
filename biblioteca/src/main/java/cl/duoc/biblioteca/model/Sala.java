package cl.duoc.biblioteca.model;

import jakarta.persistence.*;

@Entity
@Table(name = "sala")
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Integer codigo;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "capacidad")
    private Integer capacidad;

    @Column(name = "id_instituto")
    private Integer idInstituto;

    // Relación Muchos a Uno con TipoSala (id_tipo)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo", referencedColumnName = "id_tipo")
    private TipoSala tipoSala;

    // Constructor vacío requerido por JPA
    public Sala() {
    }

    // Constructor con parámetros
    public Sala(String nombre, Integer capacidad, Integer idInstituto, TipoSala tipoSala) {
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.idInstituto = idInstituto;
        this.tipoSala = tipoSala;
    }

    // --- Getters y Setters ---

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo; ) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public Integer getIdInstituto() {
        return idInstituto;
    }

    public void setIdInstituto(Integer idInstituto) {
        this.idInstituto = idInstituto;
    }

    public TipoSala getTipoSala() {
        return tipoSala;
    }

    public void setTipoSala(TipoSala tipoSala) {
        this.tipoSala = tipoSala;
    }
}