package org.example.controller;

import org.example.dto.DiaDTO;
import org.example.dto.LocalidadDto;
import org.example.entity.Dia;
import org.example.entity.Localidad;
import org.example.services.DiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/dias")
@CrossOrigin(origins = "*")

public class DiaController {
    @Autowired
    DiaService diaService;

    //POST Crear Dia
    @PostMapping
    public ResponseEntity<?> crearDia(@RequestBody DiaDTO diaDTO){
        try{
            //Esto deberia manejarse en el servicio, pero por simplicidad lo hacemos aqui
            DiaDTO dia = diaService.crearDia(diaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(dia);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el dia: " + e.getMessage());
        }
    }

    //PUT Modificar Dia
    @PutMapping("/{cod}")
    public ResponseEntity<?> modificarDia(@PathVariable("cod") Long  codDia,
                                                @RequestBody String nombreDia) {
        try {
            DiaDTO dia = diaService.modificarDia(codDia, nombreDia);
            return ResponseEntity.ok(dia);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al modificar el dia: " + e.getMessage());
        }
    }

    // PUT: Dar de baja localidad  por codigo
    @PutMapping("/{cod}/baja")
    public ResponseEntity<?> bajaDia(@PathVariable("cod") Long codDia) {
        try {
            DiaDTO diaBaja = diaService.bajaDia(codDia);
            return ResponseEntity.ok(diaBaja);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al dar de baja el dia: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getDias() {
        try {
            // Esto deberia manejarse en el servicio, pero por simplicidad lo hacemos aqui
            List<DiaDTO> dias = diaService.getDias();
            return ResponseEntity.status(HttpStatus.CREATED).body(dias);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al obtener los dias: " + e.getMessage());
        }
    }
}
