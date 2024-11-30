package com.example.application.modelo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    
    // Buscar grupos por nombre (ignorando mayúsculas/minúsculas)
    List<Grupo> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar grupos por periodo
    List<Grupo> findByPeriodo(Periodo periodo);
    
    // Buscar grupos que contengan un estudiante específico
    @Query("SELECT g FROM Grupo g JOIN g.estudiantes e WHERE e.id = :estudianteId")
    List<Grupo> findByEstudianteId(@Param("estudianteId") Long estudianteId);
    
    // Contar número de estudiantes en un grupo
    @Query("SELECT COUNT(e) FROM Grupo g JOIN g.estudiantes e WHERE g.id = :grupoId")
    long countEstudiantesByGrupoId(@Param("grupoId") Long grupoId);
    
    // Buscar grupos con cierta cantidad mínima de estudiantes
    @Query("SELECT g FROM Grupo g WHERE SIZE(g.estudiantes) >= :minEstudiantes")
    List<Grupo> findGruposConMinEstudiantes(@Param("minEstudiantes") int minEstudiantes);
    
    // Verificar si un estudiante ya está en un grupo específico
    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END FROM Grupo g " +
           "JOIN g.estudiantes e WHERE g.id = :grupoId AND e.id = :estudianteId")
    boolean existeEstudianteEnGrupo(@Param("grupoId") Long grupoId, 
                                   @Param("estudianteId") Long estudianteId);
}

