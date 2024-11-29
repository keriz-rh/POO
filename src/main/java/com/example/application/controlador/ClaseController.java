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
import com.example.application.modelo.Materia;
import com.example.application.modelo.MateriaRepository;
import com.example.application.modelo.Periodo;
import com.example.application.modelo.PeriodoRepository;
import com.example.application.modelo.Profesor2;
import com.example.application.modelo.Profesor2Repository;

import jakarta.transaction.Transactional;


@Service
public class ClaseController implements ClaseRepository {

    @Autowired
    private ClaseRepository repository;
   
    @Autowired
    private PeriodoRepository periodoRepository;
    
    @Autowired
    private MateriaRepository materiaRepository;
    
    @Autowired
    private Profesor2Repository profesorRepository;

    public List<Clase> findAll() {
        return repository.findAllWithDetails();
    }

    @Transactional
    public Clase save(Clase clase) {
        validarClase(clase);
        return repository.save(clase);
    }

    @Override
    public void flush() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'flush'");
    }

    @Override
    public <S extends Clase> S saveAndFlush(S entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAndFlush'");
    }

    @Override
    public <S extends Clase> List<S> saveAllAndFlush(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAllAndFlush'");
    }

    @Override
    public void deleteAllInBatch(Iterable<Clase> entities) {
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
    public Clase getOne(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOne'");
    }

    @Override
    public Clase getById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public Clase getReferenceById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getReferenceById'");
    }

    @Override
    public <S extends Clase> List<S> findAll(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Clase> List<S> findAll(Example<S> example, Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Clase> List<S> saveAll(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }


    @Override
    public List<Clase> findAllById(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    }

    @Override
    public Optional<Clase> findById(Long id) {
        return repository.findById(id);
    }
    

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public long count() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    @Override
    public void delete(Clase entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllById'");
    }

    @Override
    public void deleteAll(Iterable<? extends Clase> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public List<Clase> findAll(Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Page<Clase> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Clase> Optional<S> findOne(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public <S extends Clase> Page<S> findAll(Example<S> example, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Clase> long count(Example<S> example) {
        return repository.count();
    }

    @Override
    public <S extends Clase> boolean exists(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exists'");
    }

    @Override
    public <S extends Clase, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findBy'");
    }

        // Buscar por profesor
        public List<Clase> findByProfesor(Profesor2 profesor) {
            return repository.findByProfesor(profesor);
        }
    
        // Buscar por materia
        public List<Clase> findByMateria(Materia materia) {
            return repository.findByMateria(materia);
        }
    
        // Buscar por periodo
        public List<Clase> findByPeriodo(Periodo periodo) {
            return repository.findByPeriodo(periodo);
        }

    // Métodos para obtener datos para los ComboBox
    public List<Periodo> getAllPeriodos() {
        return periodoRepository.findAll();
    }

    public List<Periodo> getPeriodosActivos() {
        return periodoRepository.findPeriodosActivos();
    }

    public List<Periodo> getPeriodosPorAnio(String anio) {
        return periodoRepository.findByAnio(anio);
    }

    public List<Periodo> getPeriodosPorFecha(LocalDate fecha) {
        return periodoRepository.findPeriodosPorFecha(fecha);
    }

    public List<Materia> getAllMaterias() {
        return materiaRepository.findAll();
    }

    public List<Profesor2> getAllProfesores() {
        return profesorRepository.findAll();
    }

    private void validarClase(Clase clase) {
        List<String> errores = new ArrayList<>();

        // Validar periodo
        if (clase.getPeriodo() == null) {
            errores.add("El periodo es requerido");
        } else {
            if (!periodoRepository.existsById(clase.getPeriodo().getId())) {
                errores.add("El periodo seleccionado no existe");
            }
        }

        // Validar materia
        if (clase.getMateria() == null) {
            errores.add("La materia es requerida");
        } else {
            if (!materiaRepository.existsById(clase.getMateria().getIdMateria())) {
                errores.add("La materia seleccionada no existe");
            }
        }

        // Validar profesor
        if (clase.getProfesor() == null) {
            errores.add("El profesor es requerido");
        } else {
            if (!profesorRepository.existsById(clase.getProfesor().getId())) {
                errores.add("El profesor seleccionado no existe");
            }
        }

        // Validar horario
        if (clase.getHorario() == null) {
            errores.add("El horario es requerido");
        } else {
            // Validar que los campos del horario no sean nulos
            if (clase.getHorario().getDia() == null) {
                errores.add("El día del horario es requerido");
            }
            if (clase.getHorario().getHoraInicio() == null) {
                errores.add("La hora de inicio es requerida");
            }
            if (clase.getHorario().getHoraFin() == null) {
                errores.add("La hora de fin es requerida");
            }
            if (clase.getHorario().getAula() == null || clase.getHorario().getAula().trim().isEmpty()) {
                errores.add("El aula es requerida");
            }
            
            // Validar que la hora de fin sea posterior a la hora de inicio
            if (clase.getHorario().getHoraInicio() != null && clase.getHorario().getHoraFin() != null) {
                if (clase.getHorario().getHoraFin().isBefore(clase.getHorario().getHoraInicio())) {
                    errores.add("La hora de fin debe ser posterior a la hora de inicio");
                }
            }
        }

    // Verificar conflictos de horario para el profesor
    if (clase.getProfesor() != null && clase.getHorario() != null) {
        try {
            Long claseId = (clase.getIdClase() != null) ? clase.getIdClase() : -1L;
            boolean tieneConflicto = profesorRepository.existeConflictoHorario(
                clase.getProfesor().getId(),
                claseId,
                clase.getHorario().getDia(),
                clase.getHorario().getHoraInicio(),
                clase.getHorario().getHoraFin()
            );
            if (tieneConflicto) {
                errores.add("El profesor ya tiene una clase asignada en ese horario");
            }
        } catch (Exception e) {
            errores.add("Error al verificar conflictos de horario: " + e.getMessage());
        }
    }

        if (!errores.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", errores));
        }
    }

    @Override
    public List<Clase> findAllWithDetails() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllWithDetails'");
    }
}