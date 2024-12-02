package com.example.application.modelo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    // Obtener todas las asistencias de un estudiante específico
    List<Asistencia> findByEstudianteId(Long estudianteId);

    // Obtener todas las asistencias de un grupo específico
    List<Asistencia> findByGrupoId(Long grupoId);

    // Obtener las asistencias de un estudiante para un grupo en un rango de fechas
    List<Asistencia> findByEstudianteIdAndGrupoIdAndFechaBetween(Long estudianteId, Long grupoId, LocalDate startDate, LocalDate endDate);

    // Obtener todas las asistencias de un estudiante para un rango de fechas
    List<Asistencia> findByEstudianteIdAndFechaBetween(Long estudianteId, LocalDate startDate, LocalDate endDate);

    // Obtener todas las asistencias de un grupo para un rango de fechas
    List<Asistencia> findByGrupoIdAndFechaBetween(Long grupoId, LocalDate startDate, LocalDate endDate);

}
