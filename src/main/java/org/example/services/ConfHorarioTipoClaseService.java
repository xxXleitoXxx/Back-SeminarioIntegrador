package org.example.services;

import org.example.dto.*;
import org.example.entity.Alumno;
import org.example.entity.ConfHorarioTipoClase;
import org.example.entity.ContactoEmergencia;
import org.example.entity.HorarioiDiaxTipoClase;
import org.example.repository.ConfHorarioTipoClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@Service
public class ConfHorarioTipoClaseService {
    @Autowired
    ConfHorarioTipoClaseRepository confHorarioTipoClaseRepository;
    @Autowired
    HorarioiDiaxTipoClaseService horarioiDiaxTipoClaseService;


    public ConfHorarioTipoClaseDTO crearConfHorarioTipoClase(ConfHorarioTipoClaseDTO confHorarioTipoClaseDTO) {
        if (haySolapamientoHorarios(confHorarioTipoClaseDTO.getHorarioiDiaxTipoClaseList())) {
            throw new IllegalArgumentException("Hay solapamiento entre los horarios ingresados.");
        }

        // Dar de baja la config vigente si existe
        ConfHorarioTipoClase confActual = confHorarioTipoClaseRepository
                .findByFechaFinVigenciaConfIsNull()
                .orElse(null);

        if (confActual != null) {
            for (HorarioiDiaxTipoClase horarioiDiaxTipoClase : confActual.getHorarioiDiaxTipoClaseList()) {
                horarioiDiaxTipoClaseService.bajaHorario(horarioiDiaxTipoClase.getNroHFxTC());
            }
        }



        // Crear nueva config
        ConfHorarioTipoClase nuevaConf = new ConfHorarioTipoClase();
        nuevaConf.setFechaVigenciaConf(confHorarioTipoClaseDTO.getFechaVigenciaConf());
        nuevaConf.setFechaFinVigenciaConf(null);

        // Validar y crear horarios en memoria
        List<HorarioiDiaxTipoClase> horarioiDiaxTipoClases = new ArrayList<>();
        for (HorarioiDiaxTipoClaseDTO horarioDTO : confHorarioTipoClaseDTO.getHorarioiDiaxTipoClaseList()) {
            HorarioiDiaxTipoClase nuevoHorario = horarioiDiaxTipoClaseService.crearHorarioiDiaxTipoClase(horarioDTO);
            horarioiDiaxTipoClases.add(nuevoHorario);
        }
        // Asociar lista al padre
        nuevaConf.setHorarioiDiaxTipoClaseList(horarioiDiaxTipoClases);
        // Guardar padre (se guardan hijos en cascada)
        confHorarioTipoClaseRepository.save(nuevaConf);
        if (confActual != null) {
            bajaConfHorarioTipoClase(confActual.getNroConfTC());
        }
        confHorarioTipoClaseRepository.save(nuevaConf);
        // Convertir a DTO y retornar
        ConfHorarioTipoClaseDTO nuevaConfDTO = new ConfHorarioTipoClaseDTO();
        nuevaConfDTO.setNroConfTC(nuevaConf.getNroConfTC());
        nuevaConfDTO.setFechaVigenciaConf(nuevaConf.getFechaVigenciaConf());
        nuevaConfDTO.setFechaFinVigenciaConf(nuevaConf.getFechaFinVigenciaConf());
        List<HorarioiDiaxTipoClaseDTO> horarioDTOs = new ArrayList<>();
        for (HorarioiDiaxTipoClase horario : nuevaConf.getHorarioiDiaxTipoClaseList()) {
            HorarioiDiaxTipoClaseDTO horarioDTO = new HorarioiDiaxTipoClaseDTO();
            horarioDTO.setNroHFxTC(horario.getNroHFxTC());
            horarioDTO.setHoraDesde(horario.getHoraDesde());
            horarioDTO.setHoraHasta(horario.getHoraHasta());
            DiaDTO diaDTO = new DiaDTO();
            diaDTO.setCodDia(horario.getDia().getCodDia());
            diaDTO.setNombreDia(horario.getDia().getNombreDia());
            horarioDTO.setDiaDTO(diaDTO);
            TipoClaseDTO tipoClaseDTO = new TipoClaseDTO();
            tipoClaseDTO.setCodTipoClase(horario.getTipoClase().getCodTipoClase());
            tipoClaseDTO.setNombreTipoClase(horario.getTipoClase().getNombreTipoClase());
            horarioDTO.setTipoClase(tipoClaseDTO);
            horarioDTOs.add(horarioDTO);
        }
        nuevaConfDTO.setHorarioiDiaxTipoClaseList(horarioDTOs);
        return nuevaConfDTO;





    }

    private boolean haySolapamientoHorarios(List<HorarioiDiaxTipoClaseDTO> horarios) {
        for (int i = 0; i < horarios.size(); i++) {
            HorarioiDiaxTipoClaseDTO h1 = horarios.get(i);
            for (int j = i + 1; j < horarios.size(); j++) {
                HorarioiDiaxTipoClaseDTO h2 = horarios.get(j);
                // Check if the day is the same
                if (h1.getDiaDTO().equals(h2.getDiaDTO())) {
                    // Check if time intervals overlap
                    if (h1.getHoraDesde().before(h2.getHoraHasta()) && h1.getHoraHasta().after(h2.getHoraDesde())) {
                        return true; // Overlap on same day
                    }
                }
            }
        }
        return false;
    }


    public void bajaConfHorarioTipoClase(Long nroConfTC){
        ConfHorarioTipoClase confHorarioTipoClase = confHorarioTipoClaseRepository.findById(nroConfTC).orElseThrow(()-> new RuntimeException("No se encontro la configuracion de horario y tipo de clase"));
        confHorarioTipoClase.setFechaFinVigenciaConf(new Date());
        confHorarioTipoClaseRepository.save(confHorarioTipoClase);
    }

    public List<ConfHorarioTipoClaseDTO> getConfHorarioTipoClase() {
        List<ConfHorarioTipoClaseDTO> configList = new ArrayList<>();
        for (ConfHorarioTipoClase configsDTO :confHorarioTipoClaseRepository.findAll()){
            ConfHorarioTipoClaseDTO nuevaConfDTO = new ConfHorarioTipoClaseDTO();
            nuevaConfDTO.setNroConfTC(configsDTO.getNroConfTC());
            nuevaConfDTO.setFechaVigenciaConf(configsDTO.getFechaVigenciaConf());
            nuevaConfDTO.setFechaFinVigenciaConf(configsDTO.getFechaFinVigenciaConf());
            List<HorarioiDiaxTipoClaseDTO> horarioDTOs = new ArrayList<>();
            for (HorarioiDiaxTipoClase horario : configsDTO.getHorarioiDiaxTipoClaseList()) {
                HorarioiDiaxTipoClaseDTO horarioDTO = new HorarioiDiaxTipoClaseDTO();
                horarioDTO.setNroHFxTC(horario.getNroHFxTC());
                horarioDTO.setHoraDesde(horario.getHoraDesde());
                horarioDTO.setHoraHasta(horario.getHoraHasta());
                DiaDTO diaDTO = new DiaDTO();
                diaDTO.setCodDia(horario.getDia().getCodDia());
                diaDTO.setNombreDia(horario.getDia().getNombreDia());
                horarioDTO.setDiaDTO(diaDTO);
                TipoClaseDTO tipoClaseDTO = new TipoClaseDTO();
                tipoClaseDTO.setCodTipoClase(horario.getTipoClase().getCodTipoClase());
                tipoClaseDTO.setNombreTipoClase(horario.getTipoClase().getNombreTipoClase());
                horarioDTO.setTipoClase(tipoClaseDTO);
                horarioDTOs.add(horarioDTO);
            }
            nuevaConfDTO.setHorarioiDiaxTipoClaseList(horarioDTOs);
            configList.add(nuevaConfDTO);
        }
        return configList;
    }

}
