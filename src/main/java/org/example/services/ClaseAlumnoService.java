package org.example.services;

import org.example.dto.AlumnoDto;
import org.example.dto.ClaseAlumnoDTO;
import org.example.entity.*;
import org.example.repository.ClaseAlumnoRepository;
import org.example.repository.ClaseRepository;
import org.example.repository.InscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClaseAlumnoService {
    @Autowired
    ClaseAlumnoRepository claseAlumnoRepository;
    @Autowired
    ClaseRepository claseRepository;
    @Autowired
    InscripcionRepository inscripcionRepository;

    public void generarClaseAlumno(Long nroClase){
        //busco la clase
        Clase clase = claseRepository.findByNroClaseAndFechaBajaClaseIsNull(nroClase).orElseThrow(() -> new IllegalArgumentException("No existe una clase con el numero " + nroClase));
        TipoClase tipoClase = clase.getTipoClase();

        //busco las inscripciones activas del tipo de clase
        List<Inscripcion>  inscripcions = inscripcionRepository.findByTipoClaseAndFechaBajaInscripcionIsNull(tipoClase);
        for (Inscripcion inscripcion : inscripcions) {
            ClaseAlumno claseAlumno = new ClaseAlumno();
            claseAlumno.setClase(clase);
            claseAlumno.setAlumno(inscripcion.getAlumno());
            claseAlumnoRepository.save(claseAlumno);

        }

    }
    public void generarUnClaseAlumno(TipoClase tipoClase, Alumno alumno){
            //busco las clases activas del tipo de clase
            List<Clase> clases = claseRepository.findByTipoClaseAndFechaBajaClaseIsNull(tipoClase);
            //crear una clase alumno por cada clase activa
            for (Clase clase : clases) {
                ClaseAlumno claseAlumno = new ClaseAlumno();
                claseAlumno.setClase(clase);
                claseAlumno.setAlumno(alumno);
                claseAlumnoRepository.save(claseAlumno);

            }
    }
    public List<ClaseAlumnoDTO> getAsistenciaClaseAlumno(Long nroClase){
        Optional<Clase> clase = claseRepository.findById(nroClase);
        AlumnoDto alumnoDto=new AlumnoDto();
        List<ClaseAlumnoDTO> claseAlumnoDTOList=new ArrayList<>();

        for(ClaseAlumno claseAlumno : claseAlumnoRepository.findByClase(clase)){
            ClaseAlumnoDTO claseAlumnoDTO=new ClaseAlumnoDTO();
            claseAlumnoDTO.setNroClaseAlumno(claseAlumno.getNroClaseAlumno());
            claseAlumnoDTO.setPresenteClaseAlumno(claseAlumno.getPresenteClaseAlumno());

            alumnoDto.setNroAlumno(claseAlumno.getAlumno().getNroAlumno());
            alumnoDto.setNombreAlumno(claseAlumno.getAlumno().getNombreAlumno());
            alumnoDto.setApellidoAlumno(claseAlumno.getAlumno().getApellidoAlumno());
            alumnoDto.setDniAlumno(claseAlumno.getAlumno().getDniAlumno());

            claseAlumnoDTO.setAlumnodto(alumnoDto);
            claseAlumnoDTOList.add(claseAlumnoDTO);
        }

    return  claseAlumnoDTOList;
    }
    //recibimos un json del front con la lista de clasealumno y su estado de presente
    //por cada clasealumno seteamos el estado de presente y guardamos
    public void guardarAsistenciaClaseAlumno(List<ClaseAlumnoDTO> claseAlumnoDTOList) {
        for (ClaseAlumnoDTO claseAlumnoDTO : claseAlumnoDTOList) {
            ClaseAlumno claseAlumno = claseAlumnoRepository.findById(claseAlumnoDTO.getNroClaseAlumno())
                    .orElseThrow(() -> new IllegalArgumentException("No existe una clase alumno con el numero " + claseAlumnoDTO.getNroClaseAlumno()));
            claseAlumno.setPresenteClaseAlumno(claseAlumnoDTO.getPresenteClaseAlumno());
            claseAlumnoRepository.save(claseAlumno);
        }
    }

}
