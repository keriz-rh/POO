package com.example.application.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.HashSet;
import java.time.LocalDate;

@Entity
public class Periodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(nullable = false)
    private String nombre;

    @NotNull
    @Pattern(regexp = "\\d{4}", message = "El año debe tener 4 dígitos")
    @Column(nullable = false)
    private String anio;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column
    private Boolean activo = false;

    @ManyToMany(mappedBy = "periodos")
    private Set<Materia> materias = new HashSet<>();

    // Constructor vacío
    public Periodo() {
        this.materias = new HashSet<>();
    }

    // Constructor con parámetros básicos
    public Periodo(String nombre, String anio) {
        this.nombre = nombre;
        this.anio = anio;
    }

    // Constructor completo
    public Periodo(String nombre, String anio, LocalDate fechaInicio, LocalDate fechaFin) {
        this.nombre = nombre;
        this.anio = anio;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }


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

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(Set<Materia> materias) {
        this.materias = materias;
    }
}