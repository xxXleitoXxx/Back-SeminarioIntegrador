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

    public DiaDTO crearDia(DiaDTO diaDTO){
        //validar que no exista un dia con ese nombre y que no este dado de baja
        diaRepository.findByNombreDiaAndFechaBajaDiaIsNull(diaDTO.getNombreDia())
                .ifPresent(dia -> {
                    if (diaDTO.getFechaBajaDia() == null){
                        throw new IllegalArgumentException("Ya existe un dia con ese nombre.");
                }
                   ;
                });
        //crear dia
        Dia dia = new Dia();
        dia.setNombreDia(diaDTO.getNombreDia());
        dia.setFechaBajaDia(diaDTO.getFechaBajaDia());
        diaRepository.save(dia);

        //crear dto para devolver
        DiaDTO  diaDTO1 = new DiaDTO();
        diaDTO1.setCodDia(dia.getCodDia());
        diaDTO1.setNombreDia(dia.getNombreDia());
        diaDTO1.setFechaBajaDia(dia.getFechaBajaDia());
        return diaDTO1;
    }

    public DiaDTO modificarDia(Long codDia,String nombreDia){

        //validar que exista el dia
        Dia dia = diaRepository.findByCodDia(codDia)
                .orElseThrow(() -> new IllegalArgumentException("No existe un día con ese código."));

        //validar que no este dado de baja
        if (dia.getFechaBajaDia() != null){
            throw new IllegalArgumentException("El dia está dado de baja.");
        }

        //validar que el nuevo nombre no este en uso
        dia.setNombreDia(nombreDia);
        diaRepository.save(dia);

        //crear dto para devolver
        DiaDTO diaDTO = new DiaDTO();
        diaDTO.setCodDia(dia.getCodDia());
        diaDTO.setNombreDia(dia.getNombreDia());
        diaDTO.setFechaBajaDia(dia.getFechaBajaDia());
        return diaDTO;
    }

    public DiaDTO bajaDia(Long codDia){
        //validar que exista el dia
        Dia dia = diaRepository.findByCodDia(codDia)
                .orElseThrow(() -> new IllegalArgumentException("No existe un día con ese código."));

        //validar que no este dado de baja
        if (dia.getFechaBajaDia() != null){
            throw new IllegalArgumentException("El dia ya está dado de baja.");
        }

        //dar de baja
        dia.setFechaBajaDia(new Date());
        diaRepository.save(dia);

        //crear dto para devolver
        DiaDTO diaDTO = new DiaDTO();
        diaDTO.setCodDia(dia.getCodDia());
        diaDTO.setNombreDia(dia.getNombreDia());
        diaDTO.setFechaBajaDia(dia.getFechaBajaDia());
        return diaDTO;
    }
    public List<DiaDTO> getDias() {
        //obtener todos los dias
        List<Dia> dias = diaRepository.findAll();

        //verificar que no este vacio
        if (dias.isEmpty()) {
            throw new IllegalArgumentException("No existen dias registrados");
        }

        //convertir a dto
        List<DiaDTO>  diaDtos = new java.util.ArrayList<>();
        for (Dia dia : dias) {
            DiaDTO diaDTO = new DiaDTO();
            diaDTO.setCodDia(dia.getCodDia());
            diaDTO.setNombreDia(dia.getNombreDia());
            diaDTO.setFechaBajaDia(dia.getFechaBajaDia());
            diaDtos.add(diaDTO);
        }
        return diaDtos;

    }
}
