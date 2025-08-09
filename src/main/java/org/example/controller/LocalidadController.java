package org.example.controller;


import org.example.dto.LocalidadDto;
import org.example.dto.ProfesorDto;
import org.example.entity.Localidad;
import org.example.entity.Profesor;
import org.example.services.LocalidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/localidades")
@CrossOrigin(origins = "*")
public class LocalidadController {
    @Autowired
    private LocalidadService localidadService;

    //POST Crear Localidad
    @PostMapping
    public ResponseEntity<?>  crearLocalidad(@RequestBody LocalidadDto  localidadDto){
        try{
            //Esto deberia manejarse en el servicio, pero por simplicidad lo hacemos aqui
            Localidad localidad = localidadService.crearLocalidad(localidadDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(localidad);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear la localidad: " + e.getMessage());
        }
    }

    //PUT Modificar Localidad
    @PutMapping("/{cod}")
    public ResponseEntity<?> modificarLocalidad(@PathVariable("cod") Long  codLocalidad,
                                               @RequestBody LocalidadDto localidadDto) {
        try {
            Localidad localidad = localidadService.modificarLocalidad(codLocalidad, localidadDto);
            return ResponseEntity.ok(localidad);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al modificar la localidad: " + e.getMessage());
        }
    }

    // PUT: Dar de baja localidad  por codigo
    @PutMapping("/{cod}/baja")
    public ResponseEntity<?> bajaLocalidad(@PathVariable("cod") Long codLocalidad) {
        try {
            Localidad localidadBaja = localidadService.bajaLocalidad(codLocalidad, new Date());
            return ResponseEntity.ok(localidadBaja);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al dar de baja la localidad: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getLocalidades() {
        try{
            // Esto deberia manejarse en el servicio, pero por simplicidad lo hacemos aqui
            List<Localidad> localidades = localidadService.getLocalidades();
            return ResponseEntity.status(HttpStatus.CREATED).body(localidades);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al obtener las localidades: " + e.getMessage());
    }



}}
