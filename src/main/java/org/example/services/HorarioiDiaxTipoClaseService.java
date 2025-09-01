package org.example.services;

import org.example.dto.DiaDTO;
import org.example.dto.HorarioiDiaxTipoClaseDTO;
import org.example.dto.RangoEtarioDTO;
import org.example.dto.TipoClaseDTO;
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
            // Validar que el día exista
            Dia dia = diaRepository.findByCodDia(horarioiDiaxTipoClaseDTO.getDiaDTO().getCodDia())
                    .orElseThrow(() -> new IllegalArgumentException("Día no encontrado: " + horarioiDiaxTipoClaseDTO.getDiaDTO().getCodDia()));
            Time horaDesde;
            Time horaHasta;

            // Validar que las horas no sean nulas y que horaDesde sea anterior a horaHasta
            if (horarioiDiaxTipoClaseDTO.getHoraDesde() == null || horarioiDiaxTipoClaseDTO.getHoraHasta() == null) {
                throw new IllegalArgumentException("Las horas no pueden ser nulas");
            }

            // Validar que no haya solapamiento de horarios
            if (horarioiDiaxTipoClaseDTO.getHoraDesde().after(horarioiDiaxTipoClaseDTO.getHoraHasta()) ||
                            horarioiDiaxTipoClaseDTO.getHoraDesde().equals(horarioiDiaxTipoClaseDTO.getHoraHasta())) {
                throw new IllegalArgumentException("La hora de inicio debe ser anterior a la hora de fin.");
            }
            horaDesde = horarioiDiaxTipoClaseDTO.getHoraDesde();
            horaHasta = horarioiDiaxTipoClaseDTO.getHoraHasta();

            // Crear y guardar el nuevo horario
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
        // Buscar el horario por su número
        HorarioiDiaxTipoClase horario = horarioiDiaxTipoClaseRepository.findByNroHFxTC(nroHorario)
                .orElseThrow(() -> new IllegalArgumentException("No existe un horario con ese número."));


        if (horario.getFechaBajaHFxTC() == null) {
            horario.setFechaBajaHFxTC(new Date());
            horarioiDiaxTipoClaseRepository.save(horario);
        }
        return "El horario ha sido dado de baja exitosamente.";
    }
    public List<HorarioiDiaxTipoClaseDTO> getHorarios() {
        // Buscar la configuración por su ID
        ConfHorarioTipoClase confHorarioTipoClase =confHorarioTipoClaseRepository.findByFechaFinVigenciaConfIsNull().orElseThrow();
        // Obtener la lista de horarios asociados a la configuración
        List<HorarioiDiaxTipoClase> horarioiDiaxTipoClases = confHorarioTipoClase.getHorarioiDiaxTipoClaseList();
        // Convertir la lista de entidades a DTOs
        List<HorarioiDiaxTipoClaseDTO> horarioiDiaxTipoClaseDTOs = new ArrayList<>();
        for (HorarioiDiaxTipoClase horario : horarioiDiaxTipoClases) {
            HorarioiDiaxTipoClaseDTO dto = new HorarioiDiaxTipoClaseDTO();
            dto.setNroHFxTC(horario.getNroHFxTC());
            dto.setHoraDesde(horario.getHoraDesde());
            dto.setHoraHasta(horario.getHoraHasta());
            // Configurar el DTO de Dia
            Dia dia = horario.getDia();
            if (dia != null) {
                DiaDTO diaDTO = new DiaDTO();
                diaDTO.setCodDia(dia.getCodDia());
                diaDTO.setNombreDia(dia.getNombreDia());
                dto.setDiaDTO(diaDTO);
            }
            // Configurar el DTO de TipoClase
            TipoClase tipoClase = horario.getTipoClase();
               TipoClaseDTO tipoClaseDTO = new org.example.dto.TipoClaseDTO();
                tipoClaseDTO.setCodTipoClase(tipoClase.getCodTipoClase());
                tipoClaseDTO.setNombreTipoClase(tipoClase.getNombreTipoClase());
                tipoClaseDTO.setCupoMaxTipoClase(tipoClase.getCupoMaxTipoClase());
                tipoClaseDTO.setFechaBajaTipoClase(tipoClase.getFechaBajaTipoClase());
                RangoEtarioDTO rangoEtarioDTO = new RangoEtarioDTO();
                RangoEtario rangoEtario = tipoClase.getRangoEtario();

                rangoEtarioDTO.setCodRangoEtario(rangoEtario.getCodRangoEtario());
                rangoEtarioDTO.setNombreRangoEtario(rangoEtario.getNombreRangoEtario());
                rangoEtarioDTO.setEdadDesde(rangoEtario.getEdadDesde());
                rangoEtarioDTO.setEdadHasta(rangoEtario.getEdadHasta());
                rangoEtarioDTO.setFechaBajaRangoEtario(rangoEtario.getFechaBajaRangoEtario());
                tipoClaseDTO.setRangoEtarioDTO(rangoEtarioDTO);

                dto.setTipoClase(tipoClaseDTO);

            horarioiDiaxTipoClaseDTOs.add(dto);
        }
        return horarioiDiaxTipoClaseDTOs;
    }



}

