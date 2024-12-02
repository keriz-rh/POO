package com.example.application.modelo;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioRepository extends JpaRepository <Horario, Long> {

    List<Horario> findByDia(String dia);

    List<Horario> findByAula(String aula);

    @Query("SELECT h FROM Horario h WHERE h.horaInicio >= :inicio AND h.horaFin <= :fin")
    List<Horario> findByRangoHorario(
        @Param("inicio") LocalTime inicio, 
        @Param("fin") LocalTime fin
    );

    List<Horario> findByProfesor(Profesor2 profesor);
    
    List<Horario> findByMateria(Materia materia);
    
    List<Horario> findByPeriodo(Periodo periodo);
    
    @Query("SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END FROM Horario h " +
           "WHERE h.profesor = :profesor AND h.dia = :dia AND h.periodo = :periodo AND " +
           "((h.horaInicio <= :horaFin AND h.horaFin >= :horaInicio) OR " +
           "(h.horaInicio >= :horaInicio AND h.horaInicio < :horaFin))")
    boolean existeConflictoHorario(
        @Param("profesor") Profesor2 profesor,
        @Param("dia") String dia,
        @Param("periodo") Periodo periodo,
        @Param("horaInicio") LocalTime horaInicio,
        @Param("horaFin") LocalTime horaFin
    );

    @Query("SELECT h FROM Horario h WHERE h.profesor = :profesor AND h.periodo = :periodo")
    List<Horario> findByProfesorAndPeriodo(
        @Param("profesor") Profesor2 profesor,
        @Param("periodo") Periodo periodo
    );

    @Query("SELECT h FROM Horario h WHERE h.materia = :materia AND h.periodo = :periodo")
    List<Horario> findByMateriaAndPeriodo(
        @Param("materia") Materia materia,
        @Param("periodo") Periodo periodo
    );
    
    @Query("SELECT h FROM Horario h WHERE h.id NOT IN (SELECT gh.id FROM Grupo g JOIN g.horarios gh)")
    List<Horario> findHorariosDisponibles();

    // Consulta para obtener los horarios de un grupo por su ID
    @Query("SELECT h FROM Horario h WHERE h.grupo.id = :grupoId")
    List<Horario> findByGrupoId(@Param("grupoId") Long grupoId);
}