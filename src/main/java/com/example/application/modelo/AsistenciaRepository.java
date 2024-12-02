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

    // Obtener todas las asistencias de un periodo específico
    List<Asistencia> findByPeriodoId(Long periodoId);

    // Obtener las asistencias de un estudiante para un grupo en un rango de fechas
    List<Asistencia> findByEstudianteIdAndGrupoIdAndFechaBetween(Long estudianteId, Long grupoId, LocalDate startDate, LocalDate endDate);

    // Obtener todas las asistencias de un estudiante para un rango de fechas
    List<Asistencia> findByEstudianteIdAndFechaBetween(Long estudianteId, LocalDate startDate, LocalDate endDate);

    // Obtener todas las asistencias de un grupo para un rango de fechas
    List<Asistencia> findByGrupoIdAndFechaBetween(Long grupoId, LocalDate startDate, LocalDate endDate);

    // Obtener todas las asistencias de un grupo para un periodo específico
    List<Asistencia> findByGrupoIdAndPeriodoId(Long grupoId, Long periodoId);

    // Obtener todas las asistencias de un estudiante para un periodo específico
    List<Asistencia> findByEstudianteIdAndPeriodoId(Long estudianteId, Long periodoId);

    // Obtener todas las asistencias de un grupo, un estudiante y un periodo específico
    List<Asistencia> findByEstudianteIdAndGrupoIdAndPeriodoId(Long estudianteId, Long grupoId, Long periodoId);

    // Obtener todas las asistencias de un grupo en un rango de fechas y periodo
    List<Asistencia> findByGrupoIdAndPeriodoIdAndFechaBetween(Long grupoId, Long periodoId, LocalDate startDate, LocalDate endDate);

    // Obtener todas las asistencias de un estudiante en un rango de fechas y periodo
    List<Asistencia> findByEstudianteIdAndPeriodoIdAndFechaBetween(Long estudianteId, Long periodoId, LocalDate startDate, LocalDate endDate);

    List<Asistencia> findByGrupoIdAndPeriodoIdAndFecha(Long grupoId, Long periodoId, LocalDate fecha);

    List<Asistencia> findByEstudianteAndGrupoAndPeriodoAndFecha(Estudiante2 estudiante, Grupo grupo, Periodo periodo,
            LocalDate fecha);

    List<Asistencia> findByEstudianteCarnet(String carnet);

}
