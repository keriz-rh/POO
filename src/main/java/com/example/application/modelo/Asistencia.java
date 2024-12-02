package com.example.application.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
@Entity  // Indica que esta clase es una entidad JPA
public class Asistencia {
    
    @Id  // Marca la propiedad id como la clave primaria
    private Long id;

    @ManyToOne  // Indica que 'estudiante' es una relación muchos a uno con la entidad Estudiante2
    private Estudiante2 estudiante;   // Estudiante al que pertenece la asistencia
    
    @ManyToOne  // Indica que 'grupo' es una relación muchos a uno con la entidad Grupo
    private Grupo grupo;              // Grupo al que pertenece el estudiante
    
    private LocalDate fecha;          // Fecha de la asistencia
    private boolean presente;         // Estado de la asistencia (presente o ausente)

    // Constructor
    public Asistencia(Estudiante2 estudiante, Grupo grupo, LocalDate fecha, boolean presente) {
        this.estudiante = estudiante;
        this.grupo = grupo;
        this.fecha = fecha;
        this.presente = presente;
    }

    // Métodos getter y setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Estudiante2 getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante2 estudiante) {
        this.estudiante = estudiante;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public boolean isPresente() {
        return presente;
    }

    public void setPresente(boolean presente) {
        this.presente = presente;
    }
    
    @Override
    public String toString() {
        return "Asistencia{" +
               "id=" + id +
               ", estudiante=" + estudiante.getNombre() +  // Suponiendo que Estudiante tiene un método getNombre()
               ", grupo=" + grupo.getNombre() +            // Suponiendo que Grupo tiene un método getNombre()
               ", fecha=" + fecha +
               ", presente=" + presente +
               '}';
    }

    // Este método parece estar incompleto, puede que no sea necesario
    public void setEstado(String estado) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setEstado'");
    }
}
