package com.example.application.modelo;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;




@Entity
public class ExpedienteAcademico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Double promedioGeneral;

    @Column(length = 1000)
    private String observaciones;

    @Column(length = 500)
    private String accionesDisciplinarias;


    @OneToOne
    @JoinColumn(name = "estudiante_id")  // Nombre de la columna FK en la tabla
    private Estudiante2 estudiante;


    // Constructor vacío
    public ExpedienteAcademico() {
        this.promedioGeneral = 0.0;
    }

        // Método de utilidad para establecer la relación bidireccional
        public void setEstudianteBidireccional(Estudiante2 estudiante) {
            this.estudiante = estudiante;
            if (estudiante != null) {
                estudiante.setExpediente(this);
            }
        }    

    // Agregar getter y setter para estudiante
    public Estudiante2 getEstudiante() {
        return estudiante;
    }
    
    public void setEstudiante(Estudiante2 estudiante) {
        this.estudiante = estudiante;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPromedioGeneral() {
        return promedioGeneral;
    }

    public void setPromedioGeneral(Double promedioGeneral) {
        this.promedioGeneral = promedioGeneral;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getAccionesDisciplinarias() {
        return accionesDisciplinarias;
    }

    public void setAccionesDisciplinarias(String accionesDisciplinarias) {
        this.accionesDisciplinarias = accionesDisciplinarias;
    }
}