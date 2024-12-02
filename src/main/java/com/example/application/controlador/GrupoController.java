package com.example.application.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Service;
import com.example.application.modelo.*;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class GrupoController implements GrupoRepository{
    
    @Autowired
    private GrupoRepository repository;
    
    @Autowired
    private HorarioRepository horarioRepository;
    
    // Métodos CRUD básicos con validaciones
    @SuppressWarnings("unchecked")
    public Grupo save(Grupo grupo) {
        return repository.save(grupo);
    }

    @Transactional
    public void delete(Grupo grupo) {
        // Obtenemos el grupo con estudiantes
        Grupo grupoConEstudiantes = repository.findByIdWithEstudiantes(grupo.getId())
            .orElseThrow(() -> new IllegalStateException("Grupo no encontrado"));
        
        // Obtenemos el grupo con horarios
        Grupo grupoConHorarios = repository.findByIdWithHorarios(grupo.getId())
            .orElseThrow(() -> new IllegalStateException("Grupo no encontrado"));
    
        // Desvinculamos los estudiantes del grupo
        grupoConEstudiantes.getEstudiantes().clear();
        repository.save(grupoConEstudiantes);
    
        // Desvinculamos los horarios del grupo
        for (Horario horario : grupoConHorarios.getHorarios()) {
            horario.setGrupo(null);
            horarioRepository.save(horario);
        }
        grupoConHorarios.getHorarios().clear();
        repository.save(grupoConHorarios);
    
        repository.delete(grupo);
    }

    public List<Grupo> findAll() {
        return repository.findAll();
    }

    public List<Grupo> findAllForGrid() {
        return repository.findAllBasicInfo();
    }
    
    public Optional<Grupo> findById(Long id) {
        return repository.findById(id);
    }
    
 
    // Métodos de búsqueda
    public List<Grupo> buscarPorNombre(String nombre) {
        return repository.findByNombreContainingIgnoreCase(nombre);
    }
    
    
    private boolean tieneMateriaRepetida(Grupo grupo, Horario nuevoHorario) {
        return grupo.getHorarios().stream()
                .anyMatch(h -> h.getMateria().getIdMateria().equals(nuevoHorario.getMateria().getIdMateria()));
    }


    public void agregarEstudiante(Grupo grupo, Estudiante2 estudiante) {
        // Verificamos que el grupo exista
        Grupo grupoExistente = repository.findByIdWithEstudiantes(grupo.getId())
            .orElseThrow(() -> new IllegalStateException("Grupo no encontrado"));
        
        // Verificar capacidad
        if (grupoExistente.getEstudiantes().size() >= grupoExistente.getCapacidadEstudiantes()) {
            throw new IllegalStateException("El grupo ha alcanzado su capacidad máxima");
        }
        
        // Verificar que el estudiante no esté ya en el grupo
        if (grupoExistente.getEstudiantes().contains(estudiante)) {
            throw new IllegalStateException("El estudiante ya está en este grupo");
        }
        
        // Agregar el estudiante al grupo
        grupoExistente.getEstudiantes().add(estudiante);
        
        // Guardar los cambios
        repository.save(grupoExistente);
    }

    public void agregarHorario(Grupo grupo, Horario horario) {
        // Primero obtener las entidades frescas de la base de datos
        Horario horarioFromDb = horarioRepository.findById(horario.getId())
            .orElseThrow(() -> new IllegalStateException("Horario no encontrado"));
        
        Grupo grupoFromDb = repository.findByIdWithHorarios(grupo.getId())
            .orElseThrow(() -> new IllegalStateException("Grupo no encontrado"));
        
        // Verificar que el horario no esté ya en el grupo
        if (grupoFromDb.getHorarios().contains(horarioFromDb)) {
            throw new IllegalStateException("El horario ya está asignado a este grupo");
        }
     
        // Verificar si la materia ya está asignada al grupo
        if (tieneMateriaRepetida(grupoFromDb, horarioFromDb)) {
            throw new IllegalStateException("El grupo ya tiene un horario asignado para la materia " + 
                horarioFromDb.getMateria().getNombre());
        }

        // Verificar conflictos de horario
        if (tieneConflictoHorario(grupoFromDb, horarioFromDb)) {
            throw new IllegalStateException("Existe un conflicto con otro horario del grupo");
        }
        
        // Establecer la relación bidireccional
        horarioFromDb.setGrupo(grupoFromDb);
        grupoFromDb.getHorarios().add(horarioFromDb);
        
        // Guardar ambas entidades
        horarioRepository.save(horarioFromDb);
        repository.save(grupoFromDb);
    }

    private boolean tieneConflictoHorario(Grupo grupo, Horario nuevoHorario) {
        return grupo.getHorarios().stream()
                .anyMatch(h -> h.getDia().equals(nuevoHorario.getDia()) &&
                             !h.getHoraFin().isBefore(nuevoHorario.getHoraInicio()) &&
                             !h.getHoraInicio().isAfter(nuevoHorario.getHoraFin()));
    }

    public Grupo findByIdWithDetails(Long id) {
        Grupo grupoConEstudiantes = repository.findByIdWithEstudiantes(id)
            .orElseThrow(() -> new IllegalStateException("Grupo no encontrado"));
        
        Grupo grupoConHorarios = repository.findByIdWithHorarios(id)
            .orElseThrow(() -> new IllegalStateException("Grupo no encontrado"));
        
        // Combinar los resultados
        grupoConEstudiantes.setHorarios(grupoConHorarios.getHorarios());
        
        return grupoConEstudiantes;
    }
    
    @Override
    public void flush() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'flush'");
    }


    @Override
    public <S extends Grupo> S saveAndFlush(S entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAndFlush'");
    }


    @Override
    public <S extends Grupo> List<S> saveAllAndFlush(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAllAndFlush'");
    }


    @Override
    public void deleteAllInBatch(Iterable<Grupo> entities) {
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
    public Grupo getOne(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOne'");
    }


    @Override
    public Grupo getById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }


    @Override
    public Grupo getReferenceById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getReferenceById'");
    }


    @Override
    public <S extends Grupo> List<S> findAll(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }


    @Override
    public <S extends Grupo> List<S> findAll(Example<S> example, Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }


    @Override
    public <S extends Grupo> List<S> saveAll(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }


    @Override
    public List<Grupo> findAllById(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    }


    @Override
    public boolean existsById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsById'");
    }


    @Override
    public long count() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }


    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }





    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllById'");
    }


    @Override
    public void deleteAll(Iterable<? extends Grupo> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }


    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }


    @Override
    public List<Grupo> findAll(Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }


    @Override
    public Page<Grupo> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }


    @Override
    public <S extends Grupo> Optional<S> findOne(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }


    @Override
    public <S extends Grupo> Page<S> findAll(Example<S> example, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }


    @Override
    public <S extends Grupo> long count(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }


    @Override
    public <S extends Grupo> boolean exists(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exists'");
    }


    @Override
    public <S extends Grupo, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findBy'");
    }


    @Override
    public List<Grupo> findAllBasicInfo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllBasicInfo'");
    }


    @Override
    public List<Grupo> findByNombreContainingIgnoreCase(String nombre) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByNombreContainingIgnoreCase'");
    }


    @Override
    public Optional<Grupo> findByNombreIgnoreCase(String nombre) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByNombreIgnoreCase'");
    }


    @Override
    public List<Grupo> findByEstudianteId(Long estudianteId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByEstudianteId'");
    }


    @Override
    public long countEstudiantes(Long grupoId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'countEstudiantes'");
    }


    @Override
    public List<Grupo> findByProfesorId(Long profesorId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByProfesorId'");
    }


    @Override
    public List<Grupo> findByMateriaId(Long materiaId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByMateriaId'");
    }


    @Override
    public List<Grupo> findByPeriodoId(Long periodoId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByPeriodoId'");
    }


    @Override
    public boolean existeNombreDuplicado(String nombre, Long grupoId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existeNombreDuplicado'");
    }


    @Override
    public boolean tieneConflictosHorario(Long grupoId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tieneConflictosHorario'");
    }

    @Override
    public Optional<Grupo> findByIdWithEstudiantes(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByIdWithEstudiantes'");
    }

    @Override
    public Optional<Grupo> findByIdWithHorarios(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByIdWithHorarios'");
    }


    

}