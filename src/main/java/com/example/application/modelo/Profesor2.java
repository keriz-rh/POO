package com.example.application.modelo;

import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;


@Entity
@Table(name = "profesores")
@PrimaryKeyJoinColumn(name = "persona_id")
public class Profesor2 extends Persona {

    @NotNull
    @Column(name = "especialidad", nullable = false, length = 100)
    private String especialidad;

    // Constructor
    public Profesor2() {
        super();
    }

    // Constructor con parámetros básicos
    public Profesor2(String nombre, String apellido, String especialidad) {
        super();
        setNombre(nombre);
        setApellido(apellido);
        this.especialidad = especialidad;
    }
    
    
    // Getters y Setters
    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

}