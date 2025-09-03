package org.example.services;


import org.example.dto.DiaDTO;
import org.example.entity.Dia;
import org.example.repository.ClaseRepository;
import org.example.repository.DiaRepository;
import org.example.repository.HorarioiDiaxTipoClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DiaService {
    @Autowired
    DiaRepository  diaRepository;
    @Autowired
    HorarioiDiaxTipoClaseRepository horarioiDiaxTipoClaseRepository;
    @Autowired
    ClaseRepository claseRepository;

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

        //si el nombre es distinto al actual, validar que no exista otro dia con ese nombre
        if (!dia.getNombreDia().equals(nombreDia)){
            diaRepository.findByNombreDiaAndFechaBajaDiaIsNull(nombreDia)
                    .ifPresent(d -> {
                        throw new IllegalArgumentException("Ya existe un dia con ese nombre.");
                    });
        }

        //verificar que no esta relacionado a ningun horario
        if (horarioiDiaxTipoClaseRepository.existsByDiaAndFechaBajaHFxTCIsNull(dia)) {
            throw new IllegalArgumentException("No se puede modificar el día porque está asociado a un horario.");
        }

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

        //verificar que no esta relacionado a ningun horario
        if (horarioiDiaxTipoClaseRepository.existsByDiaAndFechaBajaHFxTCIsNull(dia)) {
            throw new IllegalArgumentException("No se puede modificar el día porque está asociado a un horario.");
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
