package com.example.application.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository <Pago, Long> {

    @Query("SELECT SUM(p.monto) FROM Pago p")
    BigDecimal sumTotalPagos();

    List<Pago> findByFechaPago(LocalDateTime fechaPago);

    List<Pago> findByFechaPagoBetween(LocalDateTime inicio, LocalDateTime fin);
    
    @Query("SELECT p FROM Pago p JOIN FETCH p.estudiante")
    List<Pago> findAllWithEstudiante();
}
