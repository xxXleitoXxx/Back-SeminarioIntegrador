package org.example.controller;

import org.example.services.FichaMedicaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/fichaMedica")
@CrossOrigin(origins = "*")
public class FichaMedicaController {
    @Autowired
    FichaMedicaServices fichaMedicaServices;
    @GetMapping("/{alumnoId}")
        public ResponseEntity<?> getFichaMedicasPorAlumno (@PathVariable Long alumnoId) {
        try {
            // Esto deberia manejarse en el servicio, pero por simplicidad lo hacemos aqui
            return ResponseEntity.status(HttpStatus.CREATED).body(fichaMedicaServices.getFichaMedicasPorAlumno(alumnoId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al obtener los alumnos: " + e.getMessage());
        }

    }

}
