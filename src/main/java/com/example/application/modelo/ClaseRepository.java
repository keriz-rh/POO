package com.example.application.modelo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.application.modelo.Clase;
import com.example.application.modelo.Profesor2;
import com.example.application.modelo.Materia;
import com.example.application.modelo.Periodo;
import java.util.List;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {
    @Query("SELECT DISTINCT c FROM Clase c " +
           "LEFT JOIN FETCH c.periodo p " +
           "LEFT JOIN FETCH c.materia m " +
           "LEFT JOIN FETCH c.profesor pr " +
           "LEFT JOIN FETCH c.horario h")
    List<Clase> findAllWithDetails();

    @Query("SELECT DISTINCT c FROM Clase c " +
           "LEFT JOIN FETCH c.periodo p " +
           "LEFT JOIN FETCH c.materia m " +
           "LEFT JOIN FETCH c.profesor pr " +
           "LEFT JOIN FETCH c.horario h " +
           "WHERE c.profesor = :profesor")
    List<Clase> findByProfesor(@Param("profesor") Profesor2 profesor);

    @Query("SELECT DISTINCT c FROM Clase c " +
           "LEFT JOIN FETCH c.periodo p " +
           "LEFT JOIN FETCH c.materia m " +
           "LEFT JOIN FETCH c.profesor pr " +
           "LEFT JOIN FETCH c.horario h " +
           "WHERE c.materia = :materia")
    List<Clase> findByMateria(@Param("materia") Materia materia);

    @Query("SELECT DISTINCT c FROM Clase c " +
           "LEFT JOIN FETCH c.periodo p " +
           "LEFT JOIN FETCH c.materia m " +
           "LEFT JOIN FETCH c.profesor pr " +
           "LEFT JOIN FETCH c.horario h " +
           "WHERE c.periodo = :periodo")
    List<Clase> findByPeriodo(@Param("periodo") Periodo periodo);
}