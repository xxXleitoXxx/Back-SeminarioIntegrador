package org.example.services;

import org.example.dto.ConfHorarioTipoClaseDTO;
import org.example.dto.ContactoEmergenciaDTO;
import org.example.dto.HorarioiDiaxTipoClaseDTO;
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


    public ConfHorarioTipoClase crearConfHorarioTipoClase(ConfHorarioTipoClaseDTO confHorarioTipoClaseDTO) {
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

        // Crear horarios y agregarlos a la lista del padre
        List<HorarioiDiaxTipoClase> horarioiDiaxTipoClases = new ArrayList<>();
        for (HorarioiDiaxTipoClaseDTO horarioDTO : confHorarioTipoClaseDTO.getHorarioiDiaxTipoClaseList()) {
            HorarioiDiaxTipoClase nuevoHorario = horarioiDiaxTipoClaseService.crearHorarioiDiaxTipoClase(horarioDTO);
            horarioiDiaxTipoClases.add(nuevoHorario);
        }

        // Asociar lista al padre
        nuevaConf.setHorarioiDiaxTipoClaseList(horarioiDiaxTipoClases);
        if (confActual != null) {
            bajaConfHorarioTipoClase(confActual.getNroConfTC());
        }
        // Guardar padre (se guardan hijos en cascada)
        return confHorarioTipoClaseRepository.save(nuevaConf);
    }


    public void bajaConfHorarioTipoClase(Long nroConfTC){
        ConfHorarioTipoClase confHorarioTipoClase = confHorarioTipoClaseRepository.findById(nroConfTC).orElseThrow(()-> new RuntimeException("No se encontro la configuracion de horario y tipo de clase"));
        confHorarioTipoClase.setFechaFinVigenciaConf(new Date());
        confHorarioTipoClaseRepository.save(confHorarioTipoClase);
    }

    public List<ConfHorarioTipoClase> getConfHorarioTipoClase() {
        return confHorarioTipoClaseRepository.findAll();
    }
}
