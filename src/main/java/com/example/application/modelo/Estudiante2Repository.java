package com.example.application.modelo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Estudiante2Repository extends JpaRepository <Estudiante2, Long>{

    List<Estudiante2> findByNivelAcademico(String nivelAcademico);

    Optional<Estudiante2> findByCarnet(String carnet);

    List<Estudiante2> findByNombrePadre(String nombrePadre);
    
    List<Estudiante2> findByFechaInscripcionBetween(
    LocalDate inicio, LocalDate fin);
    
}
