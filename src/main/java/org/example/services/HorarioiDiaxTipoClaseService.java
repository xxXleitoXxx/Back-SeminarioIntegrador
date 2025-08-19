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
            boolean existeSolapado = horarioiDiaxTipoClaseRepository
                    .existsByDiaAndFechaBajaHFxTCIsNullAndHoraDesdeLessThanAndHoraHastaGreaterThan(
                            dia,
                            Time.valueOf(horarioiDiaxTipoClaseDTO.getHoraHasta()),
                            Time.valueOf(horarioiDiaxTipoClaseDTO.getHoraDesde())
                    );

            if (existeSolapado) {
                throw new IllegalArgumentException(
                        "El horario " + horarioiDiaxTipoClaseDTO.getHoraDesde() + " - " + horarioiDiaxTipoClaseDTO.getHoraHasta() +
                                " se solapa con otro horario en el día: " + horarioiDiaxTipoClaseDTO.getDiaDTO().getCodDia()
                );
            }
            HorarioiDiaxTipoClase nuevoHorario = new HorarioiDiaxTipoClase();
            nuevoHorario.setHoraDesde(Time.valueOf(horarioiDiaxTipoClaseDTO.getHoraDesde()));
            nuevoHorario.setHoraHasta(Time.valueOf(horarioiDiaxTipoClaseDTO.getHoraHasta()));
            nuevoHorario.setDia(dia);
            TipoClase tipoClase = tipoClaseRepository.findBycodTipoClase(horarioiDiaxTipoClaseDTO.getTipoClase().getCodTipoClase())
                    .orElseThrow(() -> new IllegalArgumentException("Tipo de clase no encontrado: " + horarioiDiaxTipoClaseDTO.getTipoClase().getCodTipoClase()));
            nuevoHorario.setTipoClase(tipoClase);
            return horarioiDiaxTipoClaseRepository.save(nuevoHorario);


    }

    public String bajaHorario(Long nroHorario) {
        HorarioiDiaxTipoClase horario = horarioiDiaxTipoClaseRepository.findByNroHFxTC(nroHorario)
                .orElseThrow(() -> new IllegalArgumentException("No existe un horario con ese número."));
        if (horario.getFechaBajaHFxTC() != null) {
            throw new IllegalArgumentException("El horario ya está dado de baja.");
        }
        horario.setFechaBajaHFxTC(new Date());
        horarioiDiaxTipoClaseRepository.save(horario);
        return "El horario ha sido dado de baja exitosamente.";
    }
    public List<HorarioiDiaxTipoClase> getHorarios(Long confId) {

        ConfHorarioTipoClase confHorarioTipoClase =confHorarioTipoClaseRepository.findByNroConfTC(confId).orElseThrow(() -> new NoSuchElementException("Conf no encontrada  con confId: " + confId));
        List<HorarioiDiaxTipoClase> horarioiDiaxTipoClases = confHorarioTipoClase.getHorarioiDiaxTipoClaseList();
        return horarioiDiaxTipoClases;
    }



}
