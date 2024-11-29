package com.example.application.modelo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MateriaRepository extends JpaRepository <Materia, Long> {

    Optional<Materia> findByCodigo(String codigo);

    List<Materia> findByNombreContainingIgnoreCase(String nombre);


    
}