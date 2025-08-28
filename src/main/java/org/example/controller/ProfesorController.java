package org.example.controller;

import org.example.dto.ProfesorDto;
import org.example.entity.Profesor;
import org.example.services.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/profesores")
@CrossOrigin(origins = "*")
public class ProfesorController {
    @Autowired
    private ProfesorService profesorService;
    @GetMapping
    public ResponseEntity<?> getProfesores(){
        try{
            //Esto deberia manejarse en el servicio, pero por simplicidad lo hacemos aqui
            List<ProfesorDto> profesorcreado = profesorService.getProfesores();
            return ResponseEntity.status(HttpStatus.CREATED).body(profesorcreado);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el profesor: " + e.getMessage());
        }
    }


    // POST: Crear nuevo profesor
    @PostMapping
    public ResponseEntity<?> crearProfesor(@RequestBody ProfesorDto profesorDto){
        try{
            //Esto deberia manejarse en el servicio, pero por simplicidad lo hacemos aqui
            ProfesorDto profesorcreado = profesorService.crearProfesor(profesorDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(profesorcreado);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el profesor: " + e.getMessage());
        }
    }

    // PUT: Modificar profesor por DNI
    @PutMapping("/{nro}")
    public ResponseEntity<?> modificarProfesor(@PathVariable("nro") Long nroProfesor,
                                               @RequestBody ProfesorDto profeActualizado) {
        try {
            ProfesorDto profesorModificado = profesorService.modificarProfesor(nroProfesor, profeActualizado);
            return ResponseEntity.ok(profesorModificado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al modificar el profesor: " + e.getMessage());
        }
    }

    // PUT: Dar de baja profesor por DNI
    @PutMapping("/{nro}/baja")
    public ResponseEntity<?> darDeBajaProfesor(@PathVariable("nro") Long nroProfesor) {
        try {
            ProfesorDto profesorBaja = profesorService.bajaProfesor(nroProfesor, new Date());
            return ResponseEntity.ok(profesorBaja);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al dar de baja el profesor: " + e.getMessage());
        }
    }


}
