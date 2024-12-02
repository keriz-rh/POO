package com.example.application.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.LocalDate;

@Entity
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática del id
    private Long id;

    @ManyToOne
    private Estudiante2 estudiante;   // Estudiante al que pertenece la asistencia
    
    @ManyToOne
    private Grupo grupo;              // Grupo al que pertenece el estudiante

    @ManyToOne
    private Periodo periodo;          // Periodo al que pertenece la asistencia

    private LocalDate fecha;          // Fecha de la asistencia
    private boolean presente;         // Estado de la asistencia (presente o ausente)

    // Constructor completo
    public Asistencia(Estudiante2 estudiante, Grupo grupo, Periodo periodo, LocalDate fecha, boolean presente) {
        this.estudiante = estudiante;
        this.grupo = grupo;
        this.periodo = periodo;
        this.fecha = fecha;
        this.presente = presente;
    }

    // Constructor vacío para JPA (si es necesario para persistencia)
    public Asistencia() {
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

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
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
               ", estudiante=" + (estudiante != null ? estudiante.getNombre() : "N/A") +
               ", grupo=" + (grupo != null ? grupo.getNombre() : "N/A") +
               ", periodo=" + (periodo != null ? periodo.getNombre() : "N/A") +
               ", fecha=" + fecha +
               ", presente=" + presente +
               '}';
    }

}
