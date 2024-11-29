package com.example.application.controlador;

import java.time.LocalDate;
import java.util.ArrayList;
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

import com.example.application.modelo.Clase;
import com.example.application.modelo.ClaseRepository;
import com.example.application.modelo.Evaluacion;
import com.example.application.modelo.EvaluacionRepository;

import jakarta.transaction.Transactional;


@Service
public class EvaluacionController implements EvaluacionRepository {

    @Autowired
    private EvaluacionRepository repository;

    @Autowired
    private ClaseRepository claseRepository;

    @Override
    public void flush() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'flush'");
    }

    @Override
    public <S extends Evaluacion> S saveAndFlush(S entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAndFlush'");
    }

    @Override
    public <S extends Evaluacion> List<S> saveAllAndFlush(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAllAndFlush'");
    }

    @Override
    public void deleteAllInBatch(Iterable<Evaluacion> entities) {
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
    public Evaluacion getOne(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOne'");
    }

    @Override
    public Evaluacion getById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public Evaluacion getReferenceById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getReferenceById'");
    }


    @Override
    public <S extends Evaluacion> List<S> findAll(Example<S> example, Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Evaluacion> List<S> saveAll(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }

    @Override
    public List<Evaluacion> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Evaluacion> findAllById(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    }

    @Transactional
    public Evaluacion save(Evaluacion evaluacion) {
        try {
            validarEvaluacion(evaluacion);
            return repository.save(evaluacion);
        } catch (Exception e) {
            e.printStackTrace(); // Para debug
            throw new RuntimeException("Error al guardar la evaluación: " + e.getMessage());
        }
    }

    @Override
    public Optional<Evaluacion> findById(Long id) {
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
    public void delete(Evaluacion entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllById'");
    }

    @Override
    public void deleteAll(Iterable<? extends Evaluacion> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public List<Evaluacion> findAll(Sort sort) {
        return repository.findAll();
    }

    @Override
    public Page<Evaluacion> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Evaluacion> Optional<S> findOne(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public <S extends Evaluacion> Page<S> findAll(Example<S> example, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Evaluacion> long count(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public <S extends Evaluacion> boolean exists(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exists'");
    }

    @Override
    public <S extends Evaluacion, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findBy'");
    }

        // Métodos específicos para Evaluacion

    public List<Clase> getAllClases() {
        return claseRepository.findAllWithDetails();
    }

    public List<Evaluacion> findByDia(LocalDate dia) {
        return repository.findByDia(dia);
    }

    public List<Evaluacion> findByAula(String aula) {
        return repository.findByAula(aula);
    }

    public Optional<Evaluacion> findByClase(Clase clase) {
        return repository.findByClase(clase);
    }

    @Override
    public <S extends Evaluacion> List<S> findAll(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }


    private void validarEvaluacion(Evaluacion evaluacion) {
        List<String> errores = new ArrayList<>();

        if (evaluacion.getDia() == null) {
            errores.add("El día es requerido");
        }
        if (evaluacion.getHoraInicio() == null) {
            errores.add("La hora de inicio es requerida");
        }
        if (evaluacion.getHoraFin() == null) {
            errores.add("La hora de fin es requerida");
        }
        if (evaluacion.getAula() == null || evaluacion.getAula().trim().isEmpty()) {
            errores.add("El aula es requerida");
        }
        if (evaluacion.getTipo() == null || evaluacion.getTipo().trim().isEmpty()) {
            errores.add("El tipo es requerido");
        }
        if (evaluacion.getClase() == null) {
            errores.add("La clase es requerida");
        } else {
            // Verificar que la clase exista
            if (!claseRepository.existsById(evaluacion.getClase().getIdClase())) {
                errores.add("La clase seleccionada no existe");
            }
        }

        // Validar que la hora de fin sea posterior a la hora de inicio
        if (evaluacion.getHoraInicio() != null && evaluacion.getHoraFin() != null) {
            if (evaluacion.getHoraFin().isBefore(evaluacion.getHoraInicio())) {
                errores.add("La hora de fin debe ser posterior a la hora de inicio");
            }
        }

        if (!errores.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", errores));
        }
    }

}