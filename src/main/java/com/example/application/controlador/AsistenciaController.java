package com.example.application.controlador;

import com.example.application.modelo.Asistencia;
import com.example.application.modelo.Estudiante2;
import com.example.application.modelo.AsistenciaRepository;
import com.example.application.modelo.Estudiante2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/asistencias")
public class AsistenciaController {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private Estudiante2Repository estudiante2Repository;

    // Crear una nueva asistencia
    @PostMapping
    public ResponseEntity<Asistencia> crearAsistencia(@RequestBody Asistencia asistencia) {
        Asistencia nuevaAsistencia = asistenciaRepository.save(asistencia);
        return ResponseEntity.ok(nuevaAsistencia);
    }

    // Obtener todas las asistencias de un estudiante
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<Asistencia>> obtenerAsistenciasPorEstudiante(@PathVariable Long estudianteId) {
        List<Asistencia> asistencias = asistenciaRepository.findByEstudianteId(estudianteId);
        if (asistencias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(asistencias);
    }

    // Obtener todas las asistencias de un grupo
    @GetMapping("/grupo/{grupoId}")
    public ResponseEntity<List<Asistencia>> obtenerAsistenciasPorGrupo(@PathVariable Long grupoId) {
        List<Asistencia> asistencias = asistenciaRepository.findByGrupoId(grupoId);
        if (asistencias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(asistencias);
    }

    // Obtener asistencias de un estudiante dentro de un rango de fechas
    @GetMapping("/estudiante/{estudianteId}/fecha")
    public ResponseEntity<List<Asistencia>> obtenerAsistenciasPorEstudianteYFecha(
            @PathVariable Long estudianteId,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<Asistencia> asistencias = asistenciaRepository.findByEstudianteIdAndFechaBetween(estudianteId, start, end);
        if (asistencias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(asistencias);
    }

    // Obtener asistencias de un grupo dentro de un rango de fechas
    @GetMapping("/grupo/{grupoId}/fecha")
    public ResponseEntity<List<Asistencia>> obtenerAsistenciasPorGrupoYFecha(
            @PathVariable Long grupoId,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<Asistencia> asistencias = asistenciaRepository.findByGrupoIdAndFechaBetween(grupoId, start, end);
        if (asistencias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(asistencias);
    }

    // Obtener todos los estudiantes de un grupo
    @GetMapping("/grupo/{grupoId}/estudiantes")
    public ResponseEntity<Optional<Estudiante2>> obtenerEstudiantesPorGrupo(@PathVariable Long grupoId) {
        Optional<Estudiante2> estudiantes = estudiante2Repository.findById(grupoId);
        if (estudiantes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estudiantes);
    }
}
