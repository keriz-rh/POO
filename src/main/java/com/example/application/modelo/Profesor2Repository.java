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
    
}
