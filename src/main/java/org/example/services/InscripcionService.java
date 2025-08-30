package org.example.services;

import org.example.dto.*;
import org.example.entity.*;
import org.example.repository.AlumnoRepository;
import org.example.repository.InscripcionRepository;
import org.example.repository.TipoClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

@Service
public class InscripcionService {
    @Autowired
    InscripcionRepository inscripcionRepository;
    @Autowired
    AlumnoRepository alumnoRepository;
    @Autowired
    TipoClaseRepository tipoClaseRepository;
    @Autowired
    ClaseAlumnoService claseAlumnoService;
    public InscripcionDTO inscribirAlumno(int dniAlumno, Long codTipoClase) {
        // Buscar alumno y validar que no esté dado de baja
        Alumno alumno = alumnoRepository.findByDniAlumno(dniAlumno)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));
        if (alumno.getFechaBajaAlumno() != null) {
            throw new IllegalArgumentException("Alumno dado de baja");
        }

        // Buscar tipo de clase y validar que no esté dado de baja
        TipoClase tipoClase = tipoClaseRepository.findBycodTipoClase(codTipoClase)
                .orElseThrow(() -> new IllegalArgumentException("TipoClase no encontrada"));
        if (tipoClase.getFechaBajaTipoClase() != null) {
            throw new IllegalArgumentException("TipoClase dada de baja");
        }

        // Validaciones de negocio
        existeInscripcion(alumno, tipoClase); // Ya inscripto
        edadAdecuada(alumno, tipoClase);      // Rango de edad permitido
        cupoClase(tipoClase);                 // Cupo disponible

        // Crear inscripción
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setFechaInscripcion(new Date()); // Fecha y hora actual
        inscripcion.setFechaBajaInscripcion(null);
        inscripcion.setNroInscripcion(null); // Si es autogenerado por la DB, lo podés omitir
        inscripcion.setAlumno(alumno);
        inscripcion.setTipoClase(tipoClase);
        inscripcionRepository.save(inscripcion);

        // Crear y devolver DTO
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombreAlumno(alumno.getNombreAlumno());
        alumnoDto.setDniAlumno(alumno.getDniAlumno());
        TipoClaseDTO tipoClaseDTO = new TipoClaseDTO();
        tipoClaseDTO.setNombreTipoClase(tipoClase.getNombreTipoClase());
        tipoClaseDTO.setCodTipoClase(tipoClase.getCodTipoClase());
        InscripcionDTO inscripcionDTO = new InscripcionDTO();
        inscripcionDTO.setAlumnoDto(alumnoDto);
        inscripcionDTO.setTipoClaseDTO(tipoClaseDTO);
        // Generar la claseAlumno asociada
        claseAlumnoService.generarUnClaseAlumno(tipoClase,alumno);
        return inscripcionDTO;

    }


    public void existeInscripcion(Alumno alumno,TipoClase tipoClase){
        inscripcionRepository.findByAlumnoAndTipoClaseAndFechaBajaInscripcionIsNull(alumno, tipoClase)
            .ifPresent(inscripcion -> {
                throw new IllegalArgumentException("Ya esta inscripto");
            });
    }
    public void existenInscripciones(Alumno alumno){
      List<Inscripcion> InscripcionesAlumno = inscripcionRepository.findByAlumnoAndFechaBajaInscripcionIsNull(alumno);
        if (!InscripcionesAlumno.isEmpty()){
            throw new IllegalArgumentException("El alumno tiene inscripciones activas en clases");
        }
    }


    public void edadAdecuada(Alumno alumno,TipoClase tipoClase){
        int edadAlumno = calcularEdad(alumno.getFechaNacAlumno());

        RangoEtario rangoEtario = tipoClase.getRangoEtario();

        if (edadAlumno >= rangoEtario.getEdadDesde() && edadAlumno <= rangoEtario.getEdadHasta()) {
            // Edad adecuada, continuar
        } else {
            throw new IllegalArgumentException("El alumno no cumple con la edad requerida para esta clase.");
        }
    }



    public int calcularEdad(Date fechaNacimiento) {
        LocalDate fechaNac = fechaNacimiento.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return Period.between(fechaNac, LocalDate.now()).getYears();
    }

    public void cupoClase(TipoClase tipoClase) {
        int cupoActual = 0;

        // Traer inscripciones activas para este tipo de clase
        List<Inscripcion> inscripcionesActivas =
                inscripcionRepository.findByTipoClase(tipoClase);

        // Contar las inscripciones
        for (Inscripcion inscripcion : inscripcionesActivas) {
            cupoActual++;
        }

        // Validar contra el cupo máximo
        if (cupoActual >= tipoClase.getCupoMaxTipoClase()) {
            throw new IllegalStateException("Ya no hay cupos para esta clase");
        }
    }


    public String bajaInscripcion(Long nroInscripcion){
        // Buscar inscripción y validar que no esté ya dada de baja
        Inscripcion bajaInscripto = inscripcionRepository.findByNroInscripcion(nroInscripcion).orElseThrow(() -> new IllegalArgumentException("Inscripcion no encontrada"));
        if (bajaInscripto.getFechaBajaInscripcion()  != null){
            throw new IllegalArgumentException("Inscripcion y  dada de baja");
        }
        // Dar de baja inscripción
        bajaInscripto.setFechaBajaInscripcion(new Date());
        inscripcionRepository.save(bajaInscripto);
        return "Inscripcion dada de baja correctamente.";
    }

    public List<InscripcionDTO> getInscripciones() {
        // Traer todas las inscripciones
        List<Inscripcion> inscripciones = inscripcionRepository.findAll();

        // Verificar que haya inscripciones
        if (inscripciones.isEmpty()) {
            throw new NoSuchElementException("No hay inscripciones registradas.");
        }

        // Convertir a DTO
        List<InscripcionDTO> inscripcionesDTO = new ArrayList<>();
        for (Inscripcion inscripcion : inscripciones) {
            InscripcionDTO inscripcionDTO = new InscripcionDTO();
            inscripcionDTO.setNroInscripcion(inscripcion.getNroInscripcion());
            inscripcionDTO.setFechaInscripcion(inscripcion.getFechaInscripcion());
            inscripcionDTO.setFechaBajaInscripcion(inscripcion.getFechaBajaInscripcion());

            // Obtener datos del alumno
            Alumno alumno = inscripcion.getAlumno();
            if (alumno == null){
                throw new IllegalArgumentException("El alumno asociado a la inscripción no existe.");
            }
            AlumnoDto alumnoDto = new AlumnoDto();
                alumnoDto.setNroAlumno(alumno.getNroAlumno());
                alumnoDto.setNombreAlumno(alumno.getNombreAlumno());
                alumnoDto.setDniAlumno(alumno.getDniAlumno());
                alumnoDto.setFechaNacAlumno(alumno.getFechaNacAlumno());
                alumnoDto.setFechaBajaAlumno(alumno.getFechaBajaAlumno());
                inscripcionDTO.setAlumnoDto(alumnoDto);


            // Obtener datos del tipo de clase
            TipoClase tipoClase = inscripcion.getTipoClase();
           if (tipoClase  == null) {
               throw new IllegalArgumentException("El tipo de clase asociado a la inscripción no existe.");
           }
            TipoClaseDTO tipoClaseDTO = new TipoClaseDTO();
                tipoClaseDTO.setCodTipoClase(tipoClase.getCodTipoClase());
                tipoClaseDTO.setNombreTipoClase(tipoClase.getNombreTipoClase());
                tipoClaseDTO.setCupoMaxTipoClase(tipoClase.getCupoMaxTipoClase());
                tipoClaseDTO.setFechaBajaTipoClase(tipoClase.getFechaBajaTipoClase());
                // Asignar el DTO de RangoEtario si es necesario
                if (tipoClase.getRangoEtario() != null) {
                    RangoEtario rangoEtario = tipoClase.getRangoEtario();
                    // Aquí podrías crear y asignar un RangoEtarioDTO si lo tienes definido
                    // Por ejemplo:
                     RangoEtarioDTO rangoEtarioDTO = new RangoEtarioDTO();
                     rangoEtarioDTO.setCodRangoEtario(rangoEtario.getCodRangoEtario());
                     rangoEtarioDTO.setEdadDesde(rangoEtario.getEdadDesde());
                     rangoEtarioDTO.setEdadHasta(rangoEtario.getEdadHasta());
                     rangoEtarioDTO.setNombreRangoEtario(rangoEtario.getNombreRangoEtario());
                     tipoClaseDTO.setRangoEtarioDTO(rangoEtarioDTO);
                }
                inscripcionDTO.setTipoClaseDTO(tipoClaseDTO);
            // Agregar DTO a la lista
            inscripcionesDTO.add(inscripcionDTO);
        }
        return inscripcionesDTO;
    }

}
