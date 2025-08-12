package org.example.controller;

import org.example.services.ContactoEmergenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/contactosEmergencia")
@CrossOrigin(origins = "*")
public class ContactoEmergenciaController {
@Autowired
    ContactoEmergenciaService contactoEmergenciaService;
    @GetMapping("Alumno/{alumnoId}")
    public ResponseEntity<?> obtenerContactosEmergenciaPorAlumno(Long alumnoId) {
        try {
            // Esto deberia manejarse en el servicio, pero por simplicidad lo hacemos aqui
            return ResponseEntity.status(HttpStatus.CREATED).body(contactoEmergenciaService.getContactosEmergenciaPorAlumno(alumnoId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al obtener los alumnos: " + e.getMessage());
        }

    }
    }
