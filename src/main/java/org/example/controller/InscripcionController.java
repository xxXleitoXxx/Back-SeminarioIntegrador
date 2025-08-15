package org.example.controller;

import org.example.repository.InscripcionRepository;
import org.example.services.InscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/Inscripcion")
@CrossOrigin(origins = "*")
public class InscripcionController {
@Autowired
InscripcionService inscripcionService;
    @GetMapping
    public ResponseEntity<?> getInscripciones() {
        try {
            // Esto deberia manejarse en el servicio, pero por simplicidad lo hacemos aqui
            return ResponseEntity.status(HttpStatus.CREATED).body(inscripcionService.inscribirAlumno());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al obtener los alumnos: " + e.getMessage());
        }
        @PostMapping("/{dni}/{codTipoClase}")
        public ResponseEntity<?> getInscripciones(int dni,Long codTipoClase) {
            try {
                // Esto deberia manejarse en el servicio, pero por simplicidad lo hacemos aqui
                return ResponseEntity.status(HttpStatus.CREATED).body(inscripcionService.inscribirAlumno());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al obtener los alumnos: " + e.getMessage());
            }

}
