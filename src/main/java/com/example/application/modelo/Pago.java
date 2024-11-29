package com.example.application.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column
    private LocalDateTime fechaPago;

    @NotNull
    @Column
    private BigDecimal monto;

    @NotNull
    @Column(nullable = false, length = 255)
    private String concepto;


    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FormaPago formaPago;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante2 estudiante;
    
    // Enum para formas de pago
    public enum FormaPago {
        EFECTIVO,
        TARJETA_CREDITO,
        TARJETA_DEBITO,
        TRANSFERENCIA_BANCARIA,
        CHEQUE
    }
        
    // Constructor vacío
    public Pago() {
        this.fechaPago = LocalDateTime.now();
    }

    // Constructor con parámetros
    public Pago(BigDecimal monto, String concepto, FormaPago formaPago, Estudiante2 estudiante) {
        this();
        this.monto = monto;
        this.concepto = concepto;
        this.formaPago = formaPago;
        this.estudiante = estudiante;
    }  

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public Estudiante2 getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante2 estudiante) {
        this.estudiante = estudiante;
    }

    @Override
    public String toString() {
        return "Pago{" +
                "id=" + id +
                ", fechaPago=" + fechaPago +
                ", monto=" + monto +
                ", concepto='" + concepto + '\'' +
                ", formaPago=" + formaPago +
                ", estudiante=" + (estudiante != null ? estudiante.getCarnet() : "null") +
                '}';
    }
}

