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
        //crear ReporteGralHistTipoClaseDTO y asignarle los alumnos inscriptos
        for(TipoClase tipoClase : tipoClaseRepository.findByFechaBajaTipoClaseIsNull()){
            ReporteGralHistTipoClaseDTO tipoClaseDTO = new ReporteGralHistTipoClaseDTO();
            tipoClaseDTO.setAusenteTotalClases(0);
            tipoClaseDTO.setNombreTipoClase(tipoClase.getNombreTipoClase());
            //buscamos las clases para despues buscar la asistencia
            for(Clase claseXtipoClase : claseRepository.findByTipoClaseAndFechaBajaClaseIsNull(tipoClase)){
                //buscamos las instancias de claseAlumno relacionada a la clase
                for(ClaseAlumno claseAlumno: claseAlumnoRepository.findByClase(claseXtipoClase) ){
                    if(claseAlumno.getPresenteClaseAlumno()) {
                        tipoClaseDTO.setPresenteTotalClases(tipoClaseDTO.getPresenteTotalClases() + 1);
                    }
                    else{
                        tipoClaseDTO.setAusenteTotalClases(tipoClaseDTO.getAusenteTotalClases() + 1);
                    }
                }
                int presente = tipoClaseDTO.getPresenteTotalClases();
                int ausente = tipoClaseDTO.getAusenteTotalClases();
                int total = presente + ausente;
                if(total>0){
                    tipoClaseDTO.setPorcentajeAsistencia((double) (presente * 100) /total);
                } else{
                    tipoClaseDTO.setPorcentajeAsistencia(0);
                }

                //buscamos los inscriptos a las clases
                for(Inscripcion inscripcion : inscripcionRepository.findByTipoClase(tipoClase)){

                        tipoClaseDTO.setInscriptos(tipoClaseDTO.getInscriptos()+1);

                }
                reporteGralHistDTO.setAlumnoTotalesInscriptos(reporteGral().getAlumnoTotalesInscriptos()+tipoClaseDTO.getInscriptos());
                List<Inscripcion> incripcionestotales= inscripcionRepository.findAll();
                reporteGralHistDTO.setAlumnoTotalesNoInscriptos(reporteGral().getAlumnoTotalesInscriptos()-incripcionestotales.size());

            }
tiposClasesDto.add(tipoClaseDTO);
        }
        reporteGralHistDTO.setAlumnoTotalesActivos(alumnoRepository.findAll().size());
        reporteGralHistDTO.setAlumnoTotalesInactivos(alumnoRepository.findByFechaBajaAlumnoIsNull().size());
        reporteGralHistDTO.setReporteXLocalidad(localidadesDto);
        reporteGralHistDTO.setReporteXTipoClase(tiposClasesDto);


        return new ReporteGralHistDTO();


    }

}

