package com.example.application.modelo;

import java.time.LocalTime;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface Profesor2Repository extends JpaRepository <Profesor2, Long> {

    List<Profesor2> findByEspecialidad(String especialidad);

    @Query("SELECT p FROM Profesor2 p WHERE SIZE(p.clasesAsignadas) < :maxClases")
    List<Profesor2> findProfesoresDisponibles(@Param("maxClases") int maxClases);

    @Query("SELECT p FROM Profesor2 p JOIN p.clasesAsignadas c GROUP BY p HAVING COUNT(c) > :minClases")
    List<Profesor2> findProfesoresConMasClases(@Param("minClases") int minClases);

    @Query("SELECT COUNT(c) FROM Profesor2 p JOIN p.clasesAsignadas c WHERE p.id = :profesorId")
    long countClasesAsignadas(@Param("profesorId") Long profesorId);

    @Query("SELECT CASE WHEN COUNT(c1) > 0 THEN true ELSE false END " +
       "FROM Profesor2 p " +
       "JOIN p.clasesAsignadas c1 " +
       "JOIN c1.horario h1 " +
       "WHERE p.id = :profesorId " +
       "AND (:claseId = -1 OR c1.id != :claseId) " +  // Manejo especial para nuevas clases
       "AND h1.dia = :dia " +
       "AND h1.horaInicio < :horaFin " +
       "AND h1.horaFin > :horaInicio")
    boolean existeConflictoHorario(
    @Param("profesorId") Long profesorId, 
    @Param("claseId") Long claseId,
    @Param("dia") String dia,
    @Param("horaInicio") LocalTime horaInicio,
    @Param("horaFin") LocalTime horaFin
    );

    
}
