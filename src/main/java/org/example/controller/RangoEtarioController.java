package org.example.controller;


import org.example.dto.LocalidadDto;
import org.example.dto.RangoEtarioDTO;

import org.example.entity.Localidad;
import org.example.entity.RangoEtario;

import org.example.services.RangoEtarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/rangosetarios")
@CrossOrigin(origins = "*")

public class RangoEtarioController {

    @Autowired
    private RangoEtarioService rangoEtarioService;

    // POST: Crear nuevo rango etario
    @PostMapping
    public ResponseEntity<?> crearRangoEtario(@RequestBody RangoEtarioDTO rangoEtarioDTO){
        try{
            RangoEtarioDTO rangoetariocreadoDTO = rangoEtarioService.crearRangoEtario(rangoEtarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(rangoetariocreadoDTO);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el rango etario: " + e.getMessage());
        }
    }

    //PUT Modificar Rango Etario
    @PutMapping("/{cod}")
    public ResponseEntity<?> modificarRE(@PathVariable("cod") Long  codRangoEtario,
                                                @RequestBody RangoEtarioDTO rangoEtarioDTO) {
        try {
            RangoEtario rangoEtario = rangoEtarioService.modificarRangoEtario(codRangoEtario, rangoEtarioDTO);
            return ResponseEntity.ok(rangoEtario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al modificar el rango etario: " + e.getMessage());
        }
    }

    // PUT: Dar de baja rango etario  por codigo
    @PutMapping("/{cod}/baja")
    public ResponseEntity<?> bajaRE(@PathVariable("cod") Long codRangoEtario) {
        try {
            RangoEtarioDTO rangoEtarioBaja = rangoEtarioService.bajaRangoEtario(codRangoEtario, new Date());
            return ResponseEntity.ok(rangoEtarioBaja);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al dar de baja el rango etario: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getREs() {
        try {
            // Esto deberia manejarse en el servicio, pero por simplicidad lo hacemos aqui
            List<RangoEtarioDTO> rangoEtarios = rangoEtarioService.getRangoEtarios();
            return ResponseEntity.status(HttpStatus.CREATED).body(rangoEtarios);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al obtener los rangos etarios: " + e.getMessage());
        }
    }

}
