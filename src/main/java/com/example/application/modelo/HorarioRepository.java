package com.example.application.modelo;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioRepository extends JpaRepository <Horario, Long> {

    List<Horario> findByDia(DayOfWeek dia);

    List<Horario> findByAula(String aula);

    @Query("SELECT h FROM Horario h WHERE h.horaInicio >= :inicio AND h.horaFin <= :fin")
    List<Horario> findByRangoHorario(
        @Param("inicio") LocalTime inicio, 
        @Param("fin") LocalTime fin
    );

    
}