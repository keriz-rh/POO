package com.example.application.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Grupo")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(nullable = false)
    private String nombre;

    @Column(nullable= false)
    private int capacidadEstudiantes;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "grupo_estudiante",
        joinColumns = @JoinColumn(name = "grupo_id"),
        inverseJoinColumns = @JoinColumn(name = "estudiante_id")
    )
    private List<Estudiante2> estudiantes = new ArrayList<>();

    @OneToMany(mappedBy = "grupo", fetch = FetchType.EAGER)
    private List<Horario> horarios = new ArrayList<>();

    // Constructor
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


    public List<Estudiante2> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(List<Estudiante2> estudiantes) {
        this.estudiantes = estudiantes;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }

    public int getCapacidadEstudiantes() {
        return capacidadEstudiantes;
    }

    public void setCapacidadEstudiantes(int capacidadEstudiantes) {
        this.capacidadEstudiantes = capacidadEstudiantes;
    }
    
   
}