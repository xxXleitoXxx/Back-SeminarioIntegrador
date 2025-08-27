package org.example.services;

import org.example.dto.HorarioiDiaxTipoClaseDTO;
import org.example.entity.*;
import org.example.repository.ConfHorarioTipoClaseRepository;
import org.example.repository.DiaRepository;
import org.example.repository.HorarioiDiaxTipoClaseRepository;
import org.example.repository.TipoClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.*;

@Service
public class HorarioiDiaxTipoClaseService {
    @Autowired
    HorarioiDiaxTipoClaseRepository horarioiDiaxTipoClaseRepository;
    @Autowired
    DiaRepository diaRepository;
    @Autowired
    TipoClaseRepository tipoClaseRepository;
    @Autowired
    ConfHorarioTipoClaseRepository confHorarioTipoClaseRepository;

    public HorarioiDiaxTipoClase crearHorarioiDiaxTipoClase(HorarioiDiaxTipoClaseDTO horarioiDiaxTipoClaseDTO) {
            Dia dia = diaRepository.findByCodDia(horarioiDiaxTipoClaseDTO.getDiaDTO().getCodDia())
                    .orElseThrow(() -> new IllegalArgumentException("Día no encontrado: " + horarioiDiaxTipoClaseDTO.getDiaDTO().getCodDia()));
            Time horaDesde;
            Time horaHasta;
            if (horarioiDiaxTipoClaseDTO.getHoraDesde() == null || horarioiDiaxTipoClaseDTO.getHoraHasta() == null) {
                throw new IllegalArgumentException("Las horas no pueden ser nulas");
            }
            if (horarioiDiaxTipoClaseDTO.getHoraDesde().after(horarioiDiaxTipoClaseDTO.getHoraHasta()) ||
                            horarioiDiaxTipoClaseDTO.getHoraDesde().equals(horarioiDiaxTipoClaseDTO.getHoraHasta())) {
                throw new IllegalArgumentException("La hora de inicio debe ser anterior a la hora de fin.");
            }
            horaDesde = horarioiDiaxTipoClaseDTO.getHoraDesde();
            horaHasta = horarioiDiaxTipoClaseDTO.getHoraHasta();

            HorarioiDiaxTipoClase nuevoHorario = new HorarioiDiaxTipoClase();
            nuevoHorario.setHoraDesde(horaDesde);
            nuevoHorario.setHoraHasta(horaHasta);
            nuevoHorario.setDia(dia);
            TipoClase tipoClase = tipoClaseRepository.findBycodTipoClase(horarioiDiaxTipoClaseDTO.getTipoClase().getCodTipoClase())
                    .orElseThrow(() -> new IllegalArgumentException("Tipo de clase no encontrado: " + horarioiDiaxTipoClaseDTO.getTipoClase().getCodTipoClase()));
            nuevoHorario.setTipoClase(tipoClase);
            return horarioiDiaxTipoClaseRepository.save(nuevoHorario);


    }

    public String bajaHorario(Long nroHorario) {
        HorarioiDiaxTipoClase horario = horarioiDiaxTipoClaseRepository.findByNroHFxTC(nroHorario)
                .orElseThrow(() -> new IllegalArgumentException("No existe un horario con ese número."));
        if (horario.getFechaBajaHFxTC() == null) {
            horario.setFechaBajaHFxTC(new Date());
            horarioiDiaxTipoClaseRepository.save(horario);
        }
        return "El horario ha sido dado de baja exitosamente.";
    }
    public List<HorarioiDiaxTipoClase> getHorarios(Long confId) {

        ConfHorarioTipoClase confHorarioTipoClase =confHorarioTipoClaseRepository.findByNroConfTC(confId).orElseThrow(() -> new NoSuchElementException("Conf no encontrada  con confId: " + confId));
        List<HorarioiDiaxTipoClase> horarioiDiaxTipoClases = confHorarioTipoClase.getHorarioiDiaxTipoClaseList();
        return horarioiDiaxTipoClases;
    }



}

