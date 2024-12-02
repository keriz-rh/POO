package com.example.application.modelo;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMateria;

    @ManyToMany
    @JoinTable(
        name = "materia_periodo",
        joinColumns = @JoinColumn(name = "materia_id"),
        inverseJoinColumns = @JoinColumn(name = "periodo_id")
    )
        private Set<Periodo> periodos = new HashSet<>();

    @NotNull
    @Size(min = 2, max = 100)
    @Column(nullable = false)
    private String nombre;

    @Column(length = 20, unique = true)
    private String codigo;

    @Size(max = 500)
    @Column(length = 500)
    private String descripcion;

    
    // Constructor vacío
    public Materia() {}

    //getters y setters
    public Long getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Long idMateria) {
        this.idMateria = idMateria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Periodo> getPeriodos() { 
        return periodos;
    }

    public void setPeriodos(Set<Periodo> periodos) { 
        this.periodos = periodos;
    }

    // Métodos de utilidad para manejar relaciones
    public void agregarPeriodo(Periodo periodo) {
        this.periodos.add(periodo);  
        periodo.getMaterias().add(this); 
    }

    public void removerPeriodo(Periodo periodo) {
        this.periodos.remove(periodo);  
        periodo.getMaterias().remove(this);  
    }

}
