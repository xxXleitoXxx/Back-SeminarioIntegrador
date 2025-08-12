package org.example.controller;

import org.example.dto.AlumnoDto;
import org.example.dto.FichaMedicaDTO;
import org.example.entity.Alumno;
import org.example.entity.ContactoEmergencia;
import org.example.services.AlumnoService;
import org.example.services.FichaMedicaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "api/v1/alumnos")
@CrossOrigin(origins = "*")
public class AlumnoController {
    //Se agrega @Autowired para inyectar el servicio AlumnoServiceImp
    @Autowired
    private AlumnoService AlumnoService;
    @Autowired
    private FichaMedicaServices fichaMedicaServices;

    @GetMapping
    public ResponseEntity<?>obtenerAlumnos(){
        try {
            // Esto deberia manejarse en el servicio, pero por simplicidad lo hacemos aqui
            return ResponseEntity.status(HttpStatus.CREATED).body(AlumnoService.getAlumnos());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al obtener los alumnos: " + e.getMessage());
        }

    }
    @PostMapping
    public ResponseEntity<?> crearAlumno(@RequestBody AlumnoDto alumnoDto) {
        // Aquí puedes mapear AlumnoDto a la entidad Alumno y sus relaciones
        // Guardar el alumno en la base de datos
        try {
            Alumno alumnocreado= AlumnoService.crearAlumno(alumnoDto);
            fichaMedicaServices.agregarFichaMedica(alumnocreado.getNroAlumno(),alumnoDto.getFichaMedicaDTO());
            return ResponseEntity.status(HttpStatus.CREATED).body(alumnocreado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el alumno: " + e.getMessage());
        }
           }
    @PostMapping("/{alumnoId}/contactos")
    public ResponseEntity<ContactoEmergencia> agregarContacto(@PathVariable("alumnoId") Long alumnoId,
                                                              @RequestBody ContactoEmergencia nuevoContacto) {
        try {
            ContactoEmergencia contactoGuardado = AlumnoService.agregarContactoEmergencia(alumnoId, nuevoContacto);
            return ResponseEntity.status(HttpStatus.CREATED).body(contactoGuardado);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

}