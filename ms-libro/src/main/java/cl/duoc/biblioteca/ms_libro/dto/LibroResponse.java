package cl.duoc.biblioteca.ms_libro.dto;

public class LibroResponse {
    private Integer id;
    private String titulo;
    private AutorDTO autor;

    public LibroResponse(Integer id, String titulo, AutorDTO autor) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public AutorDTO getAutor() { return autor; }
    public void setAutor(AutorDTO autor) { this.autor = autor; }
}