package com.example.application.controlador;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Service;

import com.example.application.modelo.Pago;
import com.example.application.modelo.PagoRepository;

import jakarta.transaction.Transactional;



@Service
public class PagoController implements PagoRepository {

    @Autowired
    private PagoRepository repository;

    @Override
    public void flush() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'flush'");
    }

    @Override
    public <S extends Pago> S saveAndFlush(S entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAndFlush'");
    }

    @Override
    public <S extends Pago> List<S> saveAllAndFlush(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAllAndFlush'");
    }

    @Override
    public void deleteAllInBatch(Iterable<Pago> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllInBatch'");
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllByIdInBatch'");
    }

    @Override
    public void deleteAllInBatch() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllInBatch'");
    }

    @Override
    public Pago getOne(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOne'");
    }

    @Override
    public Pago getById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public Pago getReferenceById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getReferenceById'");
    }

    @Override
    public <S extends Pago> List<S> findAll(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }


    @Override
    public <S extends Pago> List<S> saveAll(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }


    @Override
    public List<Pago> findAllById(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional  // Para operaciones de escritura
    public Pago save(Pago pago) {
        validarPago(pago);
        return repository.save(pago);
    }

    @Override
    public Optional<Pago> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }


    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void delete(Pago entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllById'");
    }

    @Override
    public void deleteAll(Iterable<? extends Pago> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public List<Pago> findAll() {
        return repository.findAllWithEstudiante();
    }

    @Override
    public Page<Pago> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Pago> Optional<S> findOne(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public <S extends Pago> Page<S> findAll(Example<S> example, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Pago> long count(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public <S extends Pago> boolean exists(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exists'");
    }

    @Override
    public <S extends Pago, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findBy'");
    }

    // Métodos específicos para Pago
    public List<Pago> findByFechaPago(LocalDateTime fechaPago) {
        return repository.findByFechaPago(fechaPago);
    }

    public List<Pago> findByFechaPagoBetween(LocalDateTime inicio, LocalDateTime fin) {
        return repository.findByFechaPagoBetween(inicio, fin);
    }

    public BigDecimal getTotalPagos() {
        return repository.sumTotalPagos();
    }

    @Override
    public BigDecimal sumTotalPagos() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sumTotalPagos'");
    }

    @Override
    public <S extends Pago> List<S> findAll(Example<S> example, Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    // Método para validar pago
    private void validarPago(Pago pago) {
        if (pago.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
        if (pago.getEstudiante() == null) {
            throw new IllegalArgumentException("El pago debe estar asociado a un estudiante");
        }
    }

    // Método para registrar nuevo pago con validaciones
    public Pago registrarPago(Pago pago) {
        validarPago(pago);
        return repository.save(pago);
    }

    @Override
    public List<Pago> findAll(Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public List<Pago> findAllWithEstudiante() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllWithEstudiante'");
    }

}
