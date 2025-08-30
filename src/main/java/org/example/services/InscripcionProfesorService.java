package org.example.services;

import org.example.dto.*;
import org.example.entity.*;
import org.example.repository.InscripcionProfesorRepository;
import org.example.repository.ProfesorRepository;
import org.example.repository.TipoClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InscripcionProfesorService {
    @Autowired
    InscripcionProfesorRepository inscripcionProfesorRepository;
    @Autowired
    ProfesorRepository profesorRepository;
    @Autowired
    TipoClaseRepository tipoClaseRepository;

    public InscripcionProfesorDTO inscribirProfesor(int dniProfesor, Long codTipoClase) {
        // Buscar alumno y validar que no esté dado de baja
        Profesor profesor = profesorRepository.findByDniProfesorAndFechaBajaProfesorIsNull(dniProfesor)
                .orElseThrow(() -> new IllegalArgumentException("Profesor no encontrado o dado de baja"));

        // Buscar tipo de clase y validar que no esté dado de baja
        TipoClase tipoClase = tipoClaseRepository.findBycodTipoClase(codTipoClase)
                .orElseThrow(() -> new IllegalArgumentException("TipoClase no encontrada"));
        if (tipoClase.getFechaBajaTipoClase() != null) {
            throw new IllegalArgumentException("TipoClase dada de baja");
        }

        // Validaciones de negocio
        existeInscripcionProfesor(profesor, tipoClase); // Ya inscripto

        // Crear inscripción
        InscripcionProfesor inscripcionProfesor = new InscripcionProfesor();
        inscripcionProfesor.setProfesor(profesor);
        inscripcionProfesor.setTipoClase(tipoClase);
        inscripcionProfesor.setFechaInscripcionProfesor(new Date());

        inscripcionProfesorRepository.save(inscripcionProfesor);
        // Convertir a DTO y retornar
        InscripcionProfesorDTO inscripcionProfesorDTO = new InscripcionProfesorDTO();
        inscripcionProfesorDTO.setNroInscripcionProfesor(inscripcionProfesor.getNroInscripcionProfesor());
        ProfesorDto profesorDto = new ProfesorDto();
        profesorDto.setNroProfesor(profesor.getNroProfesor());
        profesorDto.setDniProfesor(profesor.getDniProfesor());
        profesorDto.setNombreProfesor(profesor.getNombreProfesor());
        profesorDto.setTelefonoProfesor(profesor.getTelefonoProfesor());
        profesorDto.setFechaBajaProfesor(profesor.getFechaBajaProfesor());
        profesorDto.setEmailProfesor(profesor.getEmailProfesor());
        inscripcionProfesorDTO.setProfesor(profesorDto);
        TipoClaseDTO tipoClaseDTO = new TipoClaseDTO();
        tipoClaseDTO.setCodTipoClase(tipoClase.getCodTipoClase());
        tipoClaseDTO.setNombreTipoClase(tipoClase.getNombreTipoClase());
        tipoClaseDTO.setCupoMaxTipoClase(tipoClase.getCupoMaxTipoClase());
        tipoClaseDTO.setFechaBajaTipoClase(tipoClase.getFechaBajaTipoClase());
        RangoEtarioDTO rangoEtarioDTO = new RangoEtarioDTO();
        rangoEtarioDTO.setCodRangoEtario(tipoClase.getRangoEtario().getCodRangoEtario());
        rangoEtarioDTO.setEdadDesde(tipoClase.getRangoEtario().getEdadDesde());
        rangoEtarioDTO.setEdadHasta(tipoClase.getRangoEtario().getEdadHasta());
        tipoClaseDTO.setRangoEtarioDTO(rangoEtarioDTO);
        inscripcionProfesorDTO.setTipoClase(tipoClaseDTO);
        inscripcionProfesorDTO.setFechaInscripcionProfesor(inscripcionProfesor.getFechaInscripcionProfesor());
        inscripcionProfesorDTO.setFechaBajaInscripcionProfesor(inscripcionProfesor.getFechaBajaInscripcionProfesor());
        return inscripcionProfesorDTO;
    }

    private void existeInscripcionProfesor(Profesor profesor, TipoClase tipoClase) {
        inscripcionProfesorRepository.findByProfesorAndTipoClaseAndFechaBajaInscripcionProfesorIsNull(profesor, tipoClase)
                .ifPresent(inscripcion -> {
                    throw new IllegalArgumentException("El profesor ya está inscripto en esa clase.");
                });
    }

    public InscripcionProfesorDTO bajaInscripcionProfesor(Long nroInscripcionProfesor) {
        InscripcionProfesor bajaInscripto = inscripcionProfesorRepository.findByNroInscripcionProfesor(nroInscripcionProfesor).orElseThrow(() -> new IllegalArgumentException("Inscripcion no encontrada"));
        if (bajaInscripto.getFechaBajaInscripcionProfesor()  != null){
            throw new IllegalArgumentException("Inscripcion y  dada de baja");
        }

        // Dar de baja la inscripción
        bajaInscripto.setFechaInscripcionProfesor(new Date());
        inscripcionProfesorRepository.save(bajaInscripto);

        // Convertir a DTO y retornar
        InscripcionProfesorDTO inscripcionProfesorDTO = new InscripcionProfesorDTO();
        inscripcionProfesorDTO.setNroInscripcionProfesor(bajaInscripto.getNroInscripcionProfesor());
        ProfesorDto profesorDto = new ProfesorDto();
        profesorDto.setNroProfesor(bajaInscripto.getProfesor().getNroProfesor());
        profesorDto.setDniProfesor(bajaInscripto.getProfesor().getDniProfesor());
        profesorDto.setNombreProfesor(bajaInscripto.getProfesor().getNombreProfesor());
        profesorDto.setTelefonoProfesor(bajaInscripto.getProfesor().getTelefonoProfesor());
        profesorDto.setFechaBajaProfesor(bajaInscripto.getProfesor().getFechaBajaProfesor());
        profesorDto.setEmailProfesor(bajaInscripto.getProfesor().getEmailProfesor());
        inscripcionProfesorDTO.setProfesor(profesorDto);
        TipoClase tipoClase = bajaInscripto.getTipoClase();
        TipoClaseDTO tipoClaseDTO = new TipoClaseDTO();
        tipoClaseDTO.setCodTipoClase(tipoClase.getCodTipoClase());
        tipoClaseDTO.setNombreTipoClase(tipoClase.getNombreTipoClase());
        tipoClaseDTO.setCupoMaxTipoClase(tipoClase.getCupoMaxTipoClase());
        tipoClaseDTO.setFechaBajaTipoClase(tipoClase.getFechaBajaTipoClase());
        RangoEtarioDTO rangoEtarioDTO = new RangoEtarioDTO();
        rangoEtarioDTO.setCodRangoEtario(tipoClase.getRangoEtario().getCodRangoEtario());
        rangoEtarioDTO.setEdadDesde(tipoClase.getRangoEtario().getEdadDesde());
        rangoEtarioDTO.setEdadHasta(tipoClase.getRangoEtario().getEdadHasta());
        tipoClaseDTO.setRangoEtarioDTO(rangoEtarioDTO);
        inscripcionProfesorDTO.setTipoClase(tipoClaseDTO);
        inscripcionProfesorDTO.setFechaInscripcionProfesor(bajaInscripto.getFechaInscripcionProfesor());
        inscripcionProfesorDTO.setFechaBajaInscripcionProfesor(bajaInscripto.getFechaBajaInscripcionProfesor());
        return inscripcionProfesorDTO;
    }
    public List<InscripcionProfesorDTO> getInscripcionesProfesores() {
        // Traer todas las inscripciones de profesores
        List<InscripcionProfesor> inscripciones = inscripcionProfesorRepository.findAll();

        // Verificar que no esté vacío
        if (inscripciones.isEmpty()) {
            throw new IllegalArgumentException("No existen inscripciones de profesores registradas");
        }

        // Convertir a DTO
        List<InscripcionProfesorDTO> inscripcionProfesorDTOS = new java.util.ArrayList<>();
        for (InscripcionProfesor inscripcion : inscripciones) {
            InscripcionProfesorDTO inscripcionProfesorDTO = new InscripcionProfesorDTO();
            inscripcionProfesorDTO.setNroInscripcionProfesor(inscripcion.getNroInscripcionProfesor());
            Profesor profesor = inscripcion.getProfesor();
            ProfesorDto profesorDto = new ProfesorDto();
            profesorDto.setNroProfesor(profesor.getNroProfesor());
            profesorDto.setDniProfesor(profesor.getDniProfesor());
            profesorDto.setNombreProfesor(profesor.getNombreProfesor());
            profesorDto.setTelefonoProfesor(profesor.getTelefonoProfesor());
            profesorDto.setFechaBajaProfesor(profesor.getFechaBajaProfesor());
            profesorDto.setEmailProfesor(profesor.getEmailProfesor());
            inscripcionProfesorDTO.setProfesor(profesorDto);
            TipoClase tipoClase = inscripcion.getTipoClase();
            TipoClaseDTO tipoClaseDTO = new TipoClaseDTO();
            tipoClaseDTO.setCodTipoClase(tipoClase.getCodTipoClase());
            tipoClaseDTO.setNombreTipoClase(tipoClase.getNombreTipoClase());
            tipoClaseDTO.setCupoMaxTipoClase(tipoClase.getCupoMaxTipoClase());
            tipoClaseDTO.setFechaBajaTipoClase(tipoClase.getFechaBajaTipoClase());
            RangoEtarioDTO rangoEtarioDTO = new RangoEtarioDTO();
            rangoEtarioDTO.setCodRangoEtario(tipoClase.getRangoEtario().getCodRangoEtario());
            rangoEtarioDTO.setEdadDesde(tipoClase.getRangoEtario().getEdadDesde());
            rangoEtarioDTO.setEdadHasta(tipoClase.getRangoEtario().getEdadHasta());
            tipoClaseDTO.setRangoEtarioDTO(rangoEtarioDTO);
            inscripcionProfesorDTO.setTipoClase(tipoClaseDTO);
            inscripcionProfesorDTO.setFechaInscripcionProfesor(inscripcion.getFechaInscripcionProfesor());
            inscripcionProfesorDTO.setFechaBajaInscripcionProfesor(inscripcion.getFechaBajaInscripcionProfesor());
            inscripcionProfesorDTOS.add(inscripcionProfesorDTO);

        }
        return inscripcionProfesorDTOS;
    }
}
