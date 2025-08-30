package org.example.controller;

import org.example.dto.InscripcionProfesorDTO;
import org.example.services.InscripcionProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/InscripcionProfesor")
@CrossOrigin(origins = "*")
public class InscripcionProfesorController {
    @Autowired
    InscripcionProfesorService inscripcionProfesorService;

    @GetMapping
    public ResponseEntity<?> getInscripcionesProfesor() {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(inscripcionProfesorService.getInscripcionesProfesores());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error al obtener las inscripciones: " + e.getMessage());
        }
    }
    @PostMapping("/{dni}/{codTipoClase}")
    public ResponseEntity<?> inscribirProfesor(
            @PathVariable int dni,
            @PathVariable Long codTipoClase) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(inscripcionProfesorService.inscribirProfesor(dni, codTipoClase));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error al inscribir profesor: " + e.getMessage());
        }
    }
    // PUT: Dar de baja inscripcion por DNI
    @PutMapping("/{nro}")
    public ResponseEntity<?> bajaInscripcionProfesor(@PathVariable("nro") Long nroInscripcionProfesor) {
        try {
            InscripcionProfesorDTO inscripcion = inscripcionProfesorService.bajaInscripcionProfesor(nroInscripcionProfesor);
            return ResponseEntity.ok(inscripcion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al dar de baja inscripcion : " + e.getMessage());
        }
    }
}
