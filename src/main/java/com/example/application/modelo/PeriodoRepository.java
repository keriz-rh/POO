package com.example.application.modelo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodoRepository extends JpaRepository <Periodo, Long> {

    List<Periodo> findByAnio(String anio);

    @Query("SELECT p FROM Periodo p WHERE p.activo = true")
    List<Periodo> findPeriodosActivos();

    @Query("SELECT p FROM Periodo p WHERE :fecha BETWEEN p.fechaInicio AND p.fechaFin")
    List<Periodo> findPeriodosPorFecha(@Param("fecha") LocalDate fecha);

    
}
