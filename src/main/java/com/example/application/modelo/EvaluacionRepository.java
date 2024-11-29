package com.example.application.modelo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluacionRepository extends JpaRepository <Evaluacion, Long> {

    @Query("SELECT e FROM Evaluacion e JOIN FETCH e.clase c " +
           "JOIN FETCH c.profesor JOIN FETCH c.materia " +
           "WHERE e.dia = :dia")
    List<Evaluacion> findByDia(@Param("dia") LocalDate dia);

    @Query("SELECT e FROM Evaluacion e JOIN FETCH e.clase c " +
           "JOIN FETCH c.profesor JOIN FETCH c.materia " +
           "WHERE e.aula = :aula")
    List<Evaluacion> findByAula(@Param("aula") String aula);

    @Query("SELECT e FROM Evaluacion e JOIN FETCH e.clase c " +
           "JOIN FETCH c.profesor JOIN FETCH c.materia")
    List<Evaluacion> findAll();
}
