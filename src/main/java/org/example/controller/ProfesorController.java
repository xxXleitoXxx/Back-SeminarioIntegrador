package org.example.controller;

import org.example.dto.ProfesorDto;
import org.example.entity.Profesor;
import org.example.services.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/profesores")
@CrossOrigin(origins = "*")
public class ProfesorController {
    @Autowired
    private ProfesorService profesorService;

    // POST: Crear nuevo profesor
    @PostMapping
    public ResponseEntity<?> crearProfesor(@RequestBody ProfesorDto profesorDto){
        try{
            Profesor profesorcreado = profesorService.crearProfesor(profesorDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(profesorcreado);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el profesor: " + e.getMessage());
        }
    }

    // PUT: Modificar profesor por DNI
    @PutMapping("/{dni}")
    public ResponseEntity<?> modificarProfesor(@PathVariable("dni") int dniProfesor,
                                               @RequestBody ProfesorDto profeActualizado) {
        try {
            Profesor profesorModificado = profesorService.modificarProfesor(dniProfesor, profeActualizado);
            return ResponseEntity.ok(profesorModificado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al modificar el profesor: " + e.getMessage());
        }
    }
}
