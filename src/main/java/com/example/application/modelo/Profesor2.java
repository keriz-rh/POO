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

    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL)
    private List<Clase> clasesAsignadas = new ArrayList<>();

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
   
    // Método de utilidad para agregar una clase
    public void agregarClase(Clase clase) {
        clasesAsignadas.add(clase);
        clase.setProfesor(this);
    }

    // Método de utilidad para remover una clase
    public void removerClase(Clase clase) {
        clasesAsignadas.remove(clase);
        clase.setProfesor(null);
    }
    
    
    // Getters y Setters
    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public List<Clase> getClasesAsignadas() {
        return clasesAsignadas;
    }

    public void setClasesAsignadas(List<Clase> clasesAsignadas) {
        this.clasesAsignadas = clasesAsignadas;
    }
}