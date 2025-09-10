package org.example.Reports;
import org.example.dto.LocalidadDto;
import org.example.entity.*;
import org.example.repository.*;
import org.example.services.LocalidadService;
import org.example.services.TipoClaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReporteGralHistService {
    @Autowired
    LocalidadRepository localidadRepository;
    @Autowired
    AlumnoRepository alumnoRepository;
    @Autowired
    TipoClaseRepository tipoClaseRepository;
    @Autowired
    ClaseRepository claseRepository;
    @Autowired
    ClaseAlumnoRepository claseAlumnoRepository;
    @Autowired
    InscripcionRepository inscripcionRepository;

    public ReporteGralHistDTO reporteGral(){
        ReporteGralHistDTO reporteGralHistDTO = new ReporteGralHistDTO();

        List<ReporteGralHistLocalidadDTO> localidadesDto = new ArrayList<>();
    //buscar localidades y por cada localidad buscar alumnos
        List<Localidad> localidades = localidadRepository.findAll();
        for (Localidad loc : localidades) {

            ReporteGralHistLocalidadDTO localidadDTO = new ReporteGralHistLocalidadDTO();
            localidadDTO.setAlumnosXLocalidadDTO(0);
            localidadDTO.setNombreLocalidad(loc.getNombreLocalidad());

            localidadDTO.setAlumnosXLocalidadDTO(alumnoRepository.findByLocalidadAlumnoAndFechaBajaAlumnoIsNull(loc).size());

            localidadesDto.add(localidadDTO);
        }


        List<ReporteGralHistTipoClaseDTO> tiposClasesDto = new ArrayList<>();
        int totalInscriptos = 0;
        int totalNoInscriptos = 0;
        //crear ReporteGralHistTipoClaseDTO y asignarle los alumnos inscriptos
        for(TipoClase tipoClase : tipoClaseRepository.findByFechaBajaTipoClaseIsNull()){
            ReporteGralHistTipoClaseDTO tipoClaseDTO = new ReporteGralHistTipoClaseDTO();
            tipoClaseDTO.setAusenteTotalClases(0);
            tipoClaseDTO.setPresenteTotalClases(0);
            tipoClaseDTO.setNombreTipoClase(tipoClase.getNombreTipoClase());
            // Acumular presentes y ausentes de todas las clases de este tipo
            for(Clase claseXtipoClase : claseRepository.findByTipoClaseAndFechaBajaClaseIsNull(tipoClase)){
                for(ClaseAlumno claseAlumno: claseAlumnoRepository.findByClase(claseXtipoClase) ){
                    Boolean presente = claseAlumno.getPresenteClaseAlumno();
                    if(Boolean.TRUE.equals(presente)) {
                        tipoClaseDTO.setPresenteTotalClases(tipoClaseDTO.getPresenteTotalClases() + 1);
                    } else {
                        tipoClaseDTO.setAusenteTotalClases(tipoClaseDTO.getAusenteTotalClases() + 1);
                    }
                }
            }
            int presente = tipoClaseDTO.getPresenteTotalClases();
            int ausente = tipoClaseDTO.getAusenteTotalClases();
            int total = presente + ausente;
            if(total > 0){
                tipoClaseDTO.setPorcentajeAsistencia((double) (presente * 100) / total);
            } else {
                tipoClaseDTO.setPorcentajeAsistencia(0);
            }
            // Calcular inscriptos solo una vez por tipo de clase
            int inscriptos = inscripcionRepository.findByTipoClaseAndFechaBajaInscripcionIsNull(tipoClase).size();
            tipoClaseDTO.setInscriptos(inscriptos);
            // Acumular para totales generales
            totalInscriptos += inscriptos;
            tiposClasesDto.add(tipoClaseDTO);
        }
        // Calcular totales generales correctamente
        int totalInscripciones = inscripcionRepository.findAll().size();
        totalNoInscriptos = totalInscriptos - totalInscripciones;
        reporteGralHistDTO.setAlumnoTotalesInscriptos(totalInscriptos);
        reporteGralHistDTO.setAlumnoTotalesNoInscriptos(totalNoInscriptos);
        reporteGralHistDTO.setAlumnoTotalesActivos(alumnoRepository.findByFechaBajaAlumnoIsNull().size());
        reporteGralHistDTO.setAlumnoTotalesInactivos(alumnoRepository.findAll().size() - alumnoRepository.findByFechaBajaAlumnoIsNull().size());
        reporteGralHistDTO.setReporteXLocalidad(localidadesDto);
        reporteGralHistDTO.setReporteXTipoClase(tiposClasesDto);
        return reporteGralHistDTO;


    }

}
