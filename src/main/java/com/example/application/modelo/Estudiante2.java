package com.example.application.modelo;

import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.time.LocalDate;


@Entity
@Table(name = "estudiantes")
@PrimaryKeyJoinColumn(name = "persona_id")
public class Estudiante2 extends Persona {

    @Column
    private String nivelAcademico;

    @Column
    private String nombrePadre;

    @NotNull
    @Column(unique = true)
    private String carnet;

    @NotNull
    @Column(nullable = false)
    private LocalDate fechaInscripcion;

    @Lob
    @Column(name = "foto", columnDefinition="MEDIUMBLOB")
    private byte[] foto;


    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL)
    private List<Pago> pagos = new ArrayList<>();

    @ManyToMany(mappedBy = "estudiantes", fetch = FetchType.EAGER)
    private List<Grupo> grupos = new ArrayList<>();

    // Getters y Setters
    public String getNivelAcademico() {
        return nivelAcademico;
    }

    public void setNivelAcademico(String nivelAcademico) {
        this.nivelAcademico = nivelAcademico;
    }

    public String getNombrePadre() {
        return nombrePadre;
    }

    public void setNombrePadre(String nombrePadre) {
        this.nombrePadre = nombrePadre;
    }

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }


    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public List<Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }
}
