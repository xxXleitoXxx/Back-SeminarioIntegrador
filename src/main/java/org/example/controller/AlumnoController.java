package org.example.controller;

import org.example.entity.Alumno;
import org.example.entity.ContactoEmergencia;
import org.example.services.AlumnoServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "api/v1/alumnos")
@CrossOrigin(origins = "*")
public class AlumnoController extends BaseControllerImpl<Alumno, AlumnoServiceImp> {

    //Añadir otro contacto de Emergencia a un Alumno
    @PostMapping("/{alumnoId}/contactos")
    public ResponseEntity<ContactoEmergencia> agregarContacto(@PathVariable("alumnoId") Long alumnoId,
                                                              @RequestBody ContactoEmergencia nuevoContacto) {
        try {
            ContactoEmergencia contactoGuardado = servicio.agregarContactoEmergencia(alumnoId, nuevoContacto);
            return ResponseEntity.status(HttpStatus.CREATED).body(contactoGuardado);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

}