package org.example.services;


import org.example.dto.DiaDTO;
import org.example.entity.Dia;
import org.example.repository.DiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DiaService {
    @Autowired
    DiaRepository  diaRepository;

    public Dia crearDia(DiaDTO diaDTO){
        diaRepository.findByNombreDia(diaDTO.getNombreDia())
                .ifPresent(dia -> {
                    if (diaDTO.getFechaBajaDia() == null){
                        throw new IllegalArgumentException("Ya existe un dia con ese nombre.");
                }
                   ;
                });
        Dia dia = new Dia();
        dia.setNombreDia(diaDTO.getNombreDia());
        dia.setFechaBajaDia(diaDTO.getFechaBajaDia());
        return diaRepository.save(dia);
    }

    public Dia modificarDia(Long codDia,String nombreDia){


        Dia dia = diaRepository.findByCodDia(codDia)
                .orElseThrow(() -> new IllegalArgumentException("No existe un día con ese código."));

        //validar que no este dado de baja
        if (dia.getFechaBajaDia() != null){
            throw new IllegalArgumentException("El dia está dado de baja.");
        }

        dia.setNombreDia(nombreDia);
        return diaRepository.save(dia);
    }

    public Dia bajaDia(Long codDia){
        Dia dia = diaRepository.findByCodDia(codDia)
                .orElseThrow(() -> new IllegalArgumentException("No existe un día con ese código."));

        //validar que no este dado de baja
        if (dia.getFechaBajaDia() != null){
            throw new IllegalArgumentException("El dia ya está dado de baja.");
        }

        dia.setFechaBajaDia(new Date());
        return diaRepository.save(dia);
    }
    public List<Dia> getDias() {
        List<Dia> dias = diaRepository.findAll();
        return dias;
    }
}
