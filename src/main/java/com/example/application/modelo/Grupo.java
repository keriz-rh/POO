package com.example.application.modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(nullable = false)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "periodo_id", nullable = false)
    private Periodo periodo;

    @ManyToMany
    @JoinTable(
        name = "grupo_estudiante",
        joinColumns = @JoinColumn(name = "grupo_id"),
        inverseJoinColumns = @JoinColumn(name = "estudiante_id")
    )
    private List<Estudiante2> estudiantes = new ArrayList<>();

    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL)
    private List<Clase> clases = new ArrayList<>();

    // Constructor vacío
    public Grupo() {}

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public List<Estudiante2> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(List<Estudiante2> estudiantes) {
        this.estudiantes = estudiantes;
    }

    public List<Clase> getClases() {
        return clases;
    }

    public void setClases(List<Clase> clases) {
        this.clases = clases;
    }

    // Métodos de utilidad para manejar relaciones
    public void agregarEstudiante(Estudiante2 estudiante) {
        estudiantes.add(estudiante);
    }

    public void removerEstudiante(Estudiante2 estudiante) {
        estudiantes.remove(estudiante);
    }

    public void agregarClase(Clase clase) {
        clases.add(clase);
        clase.setGrupo(this);
    }

    public void removerClase(Clase clase) {
        clases.remove(clase);
        clase.setGrupo(null);
    }
}
