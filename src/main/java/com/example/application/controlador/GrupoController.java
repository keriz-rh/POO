package com.example.application.controlador;

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

import com.example.application.modelo.Grupo;
import com.example.application.modelo.GrupoRepository;
import com.example.application.modelo.Periodo;

import jakarta.transaction.Transactional;

@Service
public class GrupoController implements GrupoRepository {

    @Autowired
    private GrupoRepository repository;

    @Override
    public void flush() {
        throw new UnsupportedOperationException("Unimplemented method 'flush'");
    }

    @Override
    public <S extends Grupo> S saveAndFlush(S entity) {
        throw new UnsupportedOperationException("Unimplemented method 'saveAndFlush'");
    }

    @Override
    public <S extends Grupo> List<S> saveAllAndFlush(Iterable<S> entities) {
        throw new UnsupportedOperationException("Unimplemented method 'saveAllAndFlush'");
    }

    @Override
    public void deleteAllInBatch(Iterable<Grupo> entities) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllInBatch'");
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> ids) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllByIdInBatch'");
    }

    @Override
    public void deleteAllInBatch() {
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllInBatch'");
    }

    @Override
    public Grupo getOne(Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'getOne'");
    }

    @Override
    public Grupo getById(Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public Grupo getReferenceById(Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'getReferenceById'");
    }

    @Override
    public <S extends Grupo> List<S> findAll(Example<S> example) {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Grupo> List<S> findAll(Example<S> example, Sort sort) {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Grupo> List<S> saveAll(Iterable<S> entities) {
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }

    @Override
    public List<Grupo> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Grupo> findAllById(Iterable<Long> ids) {
        throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    }

    @Transactional
    @Override
    public <S extends Grupo> S save(S entity) {
        validarGrupo(entity);
        return repository.save(entity);
    }

    @Override
    public Optional<Grupo> findById(Long id) {
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
    public void delete(Grupo entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllById'");
    }

    @Override
    public void deleteAll(Iterable<? extends Grupo> entities) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public List<Grupo> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public Page<Grupo> findAll(Pageable pageable) {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Grupo> Optional<S> findOne(Example<S> example) {
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public <S extends Grupo> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Grupo> long count(Example<S> example) {
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public <S extends Grupo> boolean exists(Example<S> example) {
        throw new UnsupportedOperationException("Unimplemented method 'exists'");
    }

    @Override
    public <S extends Grupo, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        throw new UnsupportedOperationException("Unimplemented method 'findBy'");
    }

    // Implementación de métodos específicos del repositorio
    @Override
    public List<Grupo> findByNombreContainingIgnoreCase(String nombre) {
        return repository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Grupo> findByPeriodo(Periodo periodo) {
        return repository.findByPeriodo(periodo);
    }

    @Override
    public List<Grupo> findByEstudianteId(Long estudianteId) {
        return repository.findByEstudianteId(estudianteId);
    }

    @Override
    public long countEstudiantesByGrupoId(Long grupoId) {
        return repository.countEstudiantesByGrupoId(grupoId);
    }

    @Override
    public List<Grupo> findGruposConMinEstudiantes(int minEstudiantes) {
        return repository.findGruposConMinEstudiantes(minEstudiantes);
    }

    @Override
    public boolean existeEstudianteEnGrupo(Long grupoId, Long estudianteId) {
        return repository.existeEstudianteEnGrupo(grupoId, estudianteId);
    }

    // Método de validación
    private void validarGrupo(Grupo grupo) {
        List<String> errores = new ArrayList<>();

        if (grupo.getNombre() == null || grupo.getNombre().trim().isEmpty()) {
            errores.add("El nombre del grupo es requerido");
        }

        if (grupo.getPeriodo() == null) {
            errores.add("El periodo es requerido");
        }

        if (!errores.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", errores));
        }
    }
}
