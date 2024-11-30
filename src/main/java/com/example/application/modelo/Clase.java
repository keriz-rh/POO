package com.example.application.modelo;

import jakarta.persistence.*;

@Entity
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "periodo_id", nullable = false)
    private Periodo periodo;

    @ManyToOne
    @JoinColumn(name = "materia_id")
    private Materia materia;

    @ManyToOne
    @JoinColumn(name = "profesor_id")
    private Profesor2 profesor;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "horario_id", referencedColumnName = "id")
    private Horario horario;

    @ManyToOne
    @JoinColumn(name = "grupo_id")
    private Grupo grupo;


    // Getters y Setters
    public Long getIdClase() {
        return idClase;
    }

    public void setIdClase(Long idClase) {
        this.idClase = idClase;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Profesor2 getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor2 profesor) {
        this.profesor = profesor;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

   public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %s - %s", 
            materia != null ? materia.getNombre() : "Sin materia",
            profesor != null ? profesor.getNombre() : "Sin profesor",
            horario != null ? horario.getDia() : "Sin horario",
            grupo != null ? grupo.getNombre() : "Sin grupo"
        );
    }
}
