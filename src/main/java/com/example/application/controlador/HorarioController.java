package com.example.application.controlador;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Service;

import com.example.application.modelo.Horario;
import com.example.application.modelo.HorarioRepository;
import com.example.application.modelo.Materia;
import com.example.application.modelo.Periodo;
import com.example.application.modelo.Profesor2;



@Service
public class HorarioController implements HorarioRepository {

    @Autowired
    private HorarioRepository repository;

    @Override
    public void flush() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'flush'");
    }

    @Override
    public <S extends Horario> S saveAndFlush(S entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAndFlush'");
    }

    @Override
    public <S extends Horario> List<S> saveAllAndFlush(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAllAndFlush'");
    }

    @Override
    public void deleteAllInBatch(Iterable<Horario> entities) {
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
    public Horario getOne(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOne'");
    }

    @Override
    public Horario getById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public Horario getReferenceById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getReferenceById'");
    }

    @Override
    public <S extends Horario> List<S> findAll(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Horario> List<S> findAll(Example<S> example, Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Horario> List<S> saveAll(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }

    public List<Horario> findAll() {
        try {
            return repository.findAll().stream()
                .filter(h -> h.getMateria() != null && 
                           h.getProfesor() != null && 
                           h.getPeriodo() != null)
                .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Horario> findAllById(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    }

    @SuppressWarnings("unchecked")
    public Horario save(Horario horario) {
        // Validar que el periodo esté activo
        if (!horario.getPeriodo().getActivo()) {
            throw new RuntimeException("El periodo seleccionado no está activo");
        }
        
        // Validar conflictos de horario para el profesor
        if (repository.existeConflictoHorario(
                horario.getProfesor(),
                horario.getDia(),
                horario.getPeriodo(),
                horario.getHoraInicio(),
                horario.getHoraFin())) {
            throw new RuntimeException("El profesor ya tiene un horario asignado en este periodo y horario");
        }

        // Validar horario de operación (7 AM a 10 PM)
        LocalTime inicioOperacion = LocalTime.of(7, 0);
        LocalTime finOperacion = LocalTime.of(22, 0);
        if (horario.getHoraInicio().isBefore(inicioOperacion) || 
            horario.getHoraFin().isAfter(finOperacion)) {
            throw new RuntimeException("El horario debe estar entre las 7:00 AM y 10:00 PM");
        }
        
        return repository.save(horario);
    }

    @Override
    public Optional<Horario> findById(Long id) {
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
    public void delete(Horario entity) {
        repository.delete(entity);
    }

    public List<Horario> findHorariosDisponibles() {
        List<Horario> horarios = repository.findHorariosDisponibles();
        System.out.println("Horarios disponibles encontrados: " + horarios.size());
        return horarios;
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllById'");
    }

    @Override
    public void deleteAll(Iterable<? extends Horario> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public List<Horario> findAll(Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Page<Horario> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Horario> Optional<S> findOne(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public <S extends Horario> Page<S> findAll(Example<S> example, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Horario> long count(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public <S extends Horario> boolean exists(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exists'");
    }

    @Override
    public <S extends Horario, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findBy'");
    }

    // Métodos específicos para Horario
    public List<Horario> findByDia(String dia) {
        return repository.findByDia(dia);
    }

    public List<Horario> findByAula(String aula) {
        return repository.findByAula(aula);
    }

    public List<Horario> findByRangoHorario(LocalTime inicio, LocalTime fin) {
        return repository.findByRangoHorario(inicio, fin);
    }    

    public List<Horario> findByProfesor(Profesor2 profesor) {
        return repository.findByProfesor(profesor);
    }
    
    public List<Horario> findByMateria(Materia materia) {
        return repository.findByMateria(materia);
    }
    
    public List<Horario> findByPeriodo(Periodo periodo) {
        return repository.findByPeriodo(periodo);
    }

    public List<Horario> findByProfesorAndPeriodo(Profesor2 profesor, Periodo periodo) {
        return repository.findByProfesorAndPeriodo(profesor, periodo);
    }
    
    public List<Horario> findByMateriaAndPeriodo(Materia materia, Periodo periodo) {
        return repository.findByMateriaAndPeriodo(materia, periodo);
    }

    @Override
    public boolean existeConflictoHorario(Profesor2 profesor, String dia, Periodo periodo, LocalTime horaInicio,
            LocalTime horaFin) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existeConflictoHorario'");
    }

    @Override
    public List<Horario> findByGrupoId(Long grupoId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByGrupoId'");
    }


}