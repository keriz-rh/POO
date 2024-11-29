package com.example.application.modelo;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpedienteAcademicoRepository extends JpaRepository <ExpedienteAcademico, Long> {

    // Buscar por estudiante
    Optional<ExpedienteAcademico> findByEstudiante(Estudiante2 estudiante);
    
    // Buscar por carnet de estudiante
    Optional<ExpedienteAcademico> findByEstudianteCarnet(String carnet);
    
    // Buscar expedientes con promedio mayor a cierto valor
    List<ExpedienteAcademico> findByPromedioGeneralGreaterThan(Double promedio);
    
    // Buscar expedientes con acciones disciplinarias
    List<ExpedienteAcademico> findByAccionesDisciplinariasIsNotNull();

}

