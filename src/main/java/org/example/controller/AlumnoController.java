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
    private AlumnoService alumnoService;
    @Autowired
    private FichaMedicaServices fichaMedicaServices;

    @GetMapping
    public ResponseEntity<?>obtenerAlumnos(){
        try {
            // Esto deberia manejarse en el servicio, pero por simplicidad lo hacemos aqui
            return ResponseEntity.status(HttpStatus.CREATED).body(alumnoService.getAlumnos());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al obtener los alumnos: " + e.getMessage());
        }

    }
    @PostMapping
    public ResponseEntity<?> crearAlumno(@RequestBody AlumnoDto alumnoDto) {
        // Aquí puedes mapear AlumnoDto a la entidad Alumno y sus relaciones
        // Guardar el alumno en la base de datos
        try {
            Alumno alumnocreado= alumnoService.crearAlumno(alumnoDto);

            for(FichaMedicaDTO fichamedicadto: alumnoDto.getFichaMedicaDTO()) {
                fichaMedicaServices.agregarFichaMedica(alumnocreado.getNroAlumno(),fichamedicadto);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(alumnocreado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el alumno: " + e.getMessage());
        }
           }

           @PutMapping("/{alumnoId}")
    public ResponseEntity<?> ModificarAlumno(@PathVariable("alumnoId") Long alumnoId,
                                              @RequestBody AlumnoDto alumnodto){

               try {
                   AlumnoDto alumnoModificado = alumnoService.modificarAlumno(alumnodto);
                   return ResponseEntity.status(HttpStatus.CREATED).body(alumnoModificado);
               } catch (Exception e) {
                   return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al modificar el alumno: " + e.getMessage());
               }
           }
    @PostMapping("/{alumnoId}/contactos")
    public ResponseEntity<?> agregarContacto(@PathVariable("alumnoId") Long alumnoId,
                                                              @RequestBody ContactoEmergencia nuevoContacto) {
        try {
            ContactoEmergencia contactoGuardado = alumnoService.agregarContactoEmergencia(alumnoId, nuevoContacto);
            return ResponseEntity.status(HttpStatus.CREATED).body(contactoGuardado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al dar de baja al alumno: " + e.getMessage());
        }
    }
//baja alumno
    @PutMapping("/{dniAlumno}/baja")
    public ResponseEntity<?> bajaAlumno(@PathVariable("dniAlumno") int dniAlumno) {
        try {
            Alumno alumnoBaja = alumnoService.bajaAlumno(dniAlumno);
            return ResponseEntity.ok(alumnoBaja);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al dar de baja al alumno: " + e.getMessage());
        }
    }

}