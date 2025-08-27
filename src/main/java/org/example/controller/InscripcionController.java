package org.example.controller;

import org.example.entity.Inscripcion;
import org.example.entity.Profesor;
import org.example.repository.InscripcionRepository;
import org.example.services.InscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(path = "api/v1/Inscripcion")
@CrossOrigin(origins = "*")
public class InscripcionController {

    @Autowired
    InscripcionService inscripcionService;

    @GetMapping
    public ResponseEntity<?> getInscripciones() {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(inscripcionService.getInscripciones());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error al obtener las inscripciones: " + e.getMessage());
        }
    }

    @PostMapping("/{dni}/{codTipoClase}")
    public ResponseEntity<?> inscribir(
            @PathVariable int dni,
            @PathVariable Long codTipoClase) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(inscripcionService.inscribirAlumno(dni, codTipoClase));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error al inscribir alumno: " + e.getMessage());
        }
    }

    // PUT: Dar de baja inscripcion por DNI
    @PutMapping("/{nro}")
    public ResponseEntity<?> bajaInscripcion(@PathVariable("nro") Long nroInscripcion) {
        try {
            String inscripcion = inscripcionService.bajaInscripcion(nroInscripcion);
            return ResponseEntity.ok(inscripcion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al dar de baja inscripcion : " + e.getMessage());
        }
    }
}



