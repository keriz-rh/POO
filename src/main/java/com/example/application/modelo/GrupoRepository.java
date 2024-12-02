package com.example.application.modelo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    
    // Búsquedas básicas
    @Query("SELECT new Grupo(g.id, g.nombre, g.capacidadEstudiantes) FROM Grupo g")
    List<Grupo> findAllBasicInfo();

    @Query("SELECT g FROM Grupo g LEFT JOIN FETCH g.estudiantes LEFT JOIN FETCH g.horarios WHERE g.id = :id")
    Grupo findByIdWithDetails(@Param("id") Long id);

    List<Grupo> findByNombreContainingIgnoreCase(String nombre);
    
    Optional<Grupo> findByNombreIgnoreCase(String nombre);
    
    @Query("SELECT g FROM Grupo g LEFT JOIN FETCH g.estudiantes WHERE g.id = :id")
    Optional<Grupo> findByIdWithEstudiantes(@Param("id") Long id);
    
    @Query("SELECT g FROM Grupo g LEFT JOIN FETCH g.horarios WHERE g.id = :id")
    Optional<Grupo> findByIdWithHorarios(@Param("id") Long id);

    // Búsquedas relacionadas con estudiantes
    @Query("SELECT g FROM Grupo g JOIN g.estudiantes e WHERE e.id = :estudianteId")
    List<Grupo> findByEstudianteId(@Param("estudianteId") Long estudianteId);
    
    @Query("SELECT COUNT(e) FROM Grupo g JOIN g.estudiantes e WHERE g.id = :grupoId")
    long countEstudiantes(@Param("grupoId") Long grupoId);
    
    // Búsquedas relacionadas con horarios
    @Query("SELECT DISTINCT g FROM Grupo g JOIN g.horarios h WHERE h.profesor.id = :profesorId")
    List<Grupo> findByProfesorId(@Param("profesorId") Long profesorId);
    
    @Query("SELECT DISTINCT g FROM Grupo g JOIN g.horarios h WHERE h.materia.id = :materiaId")
    List<Grupo> findByMateriaId(@Param("materiaId") Long materiaId);
    
    @Query("SELECT DISTINCT g FROM Grupo g JOIN g.horarios h WHERE h.periodo.id = :periodoId")
    List<Grupo> findByPeriodoId(@Param("periodoId") Long periodoId);
    
    // Validaciones
    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END FROM Grupo g WHERE LOWER(g.nombre) = LOWER(:nombre) AND g.id != :grupoId")
    boolean existeNombreDuplicado(@Param("nombre") String nombre, @Param("grupoId") Long grupoId);
    
    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END FROM Grupo g " +
           "JOIN g.horarios h1 WHERE g.id = :grupoId AND EXISTS (" +
           "SELECT 1 FROM Horario h2 WHERE h2.profesor = h1.profesor " +
           "AND h2.dia = h1.dia AND h2.periodo = h1.periodo " +
           "AND ((h2.horaInicio <= h1.horaFin AND h2.horaFin >= h1.horaInicio) " +
           "OR (h2.horaInicio >= h1.horaInicio AND h2.horaInicio < h1.horaFin)) " +
           "AND h2.id != h1.id)")
    boolean tieneConflictosHorario(@Param("grupoId") Long grupoId);
}