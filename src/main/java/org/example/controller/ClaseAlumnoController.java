package org.example.controller;

import org.example.dto.ClaseAlumnoDTO;
import org.example.dto.ClaseDTO;
import org.example.services.ClaseAlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/claseAlumno")
@CrossOrigin(origins = "*")
public class ClaseAlumnoController {
    @Autowired
    ClaseAlumnoService claseAlumnoService;
    @GetMapping("asistencia/{nroClase}")
    public ResponseEntity<?> getDias(@PathVariable Long nroClase) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(claseAlumnoService.getAsistenciaClaseAlumno(nroClase));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al obtener las clases: " + e.getMessage());
        }
    }
    @PostMapping("asistencia")
    public ResponseEntity<?> guardarAsistenciaClaseAlumno(List<ClaseAlumnoDTO> claseAlumnoDTOS) {
        try {
            claseAlumnoService.guardarAsistenciaClaseAlumno(claseAlumnoDTOS);
            return ResponseEntity.status(HttpStatus.CREATED).body("Se generaron las clases alumno correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al generar las clases alumno: " + e.getMessage());
        }
    }


}
