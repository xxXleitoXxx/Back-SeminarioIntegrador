package org.example.services;

import jakarta.transaction.Transactional;
import org.example.dto.ClaseDTO;
import org.example.dto.ProfesorDto;
import org.example.dto.TipoClaseDTO;
import org.example.entity.*;
import org.example.repository.ClaseRepository;
import org.example.repository.ConfHorarioTipoClaseRepository;
import org.example.repository.InscripcionProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class ClaseService {
    @Autowired
    ClaseRepository claseRepository;
    @Autowired
    ConfHorarioTipoClaseRepository confHorarioTipoClaseRepository;
    @Autowired
    ClaseAlumnoService claseAlumnoService;
    @Autowired
    InscripcionProfesorRepository inscripcionProfesorRepository;

    @Transactional
    public void generarclases() {
        // Buscar configuración activa
        ConfHorarioTipoClase confActual = confHorarioTipoClaseRepository
                .findByFechaFinVigenciaConfIsNull()
                .orElseThrow(() -> new IllegalArgumentException("No hay una configuración activa de horarios"));

        // Verificar si la fecha de vigencia es hoy
        LocalDate hoy = LocalDate.now();
        LocalDate vigencia = confActual.getFechaVigenciaConf().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // Si la vigencia es hoy, generar las clases y dar de baja las futuras
        boolean seEjecuto = false;
        if (hoy.equals(vigencia)) {

            //DAR DE BAJA TODAS LAS CLASES SIGUIENTES A HOY
            List<Clase> clasesFuturas = claseRepository.findByFechaBajaClaseIsNullAndFechaHoraClaseAfter(new Date());
            for (Clase clase : clasesFuturas) {
                clase.setFechaBajaClase(new Date());
                claseRepository.save(clase);
            }

            for (HorarioiDiaxTipoClase horarios : confActual.getHorarioiDiaxTipoClaseList()) {

                Date fechaClase = proximaFecha(horarios.getDia().getNombreDia());

                Clase clase = new Clase();
                clase.setDiaClase(horarios.getDia());
                clase.setFechaHoraClase(fechaClase);
                clase.setTipoClase(horarios.getTipoClase());
                List<Profesor> profesores = new ArrayList<>(claseProfesores(horarios.getTipoClase()));
                clase.setProfesores(profesores);

                claseRepository.save(clase);
                claseAlumnoService.generarClaseAlumno(clase.getNroClase());
            }
            seEjecuto  = true;

        }

        List<Clase> clasesHoy =
                claseRepository.findByFechaBajaClaseIsNullAndFechaHoraClase(new Date());

        List<Clase> clasesMañana =
                claseRepository.findByFechaBajaClaseIsNullAndFechaHoraClase(
                        proximaFecha(hoy.plusDays(1).getDayOfWeek())
                );
        // Si no se ejecutó hoy, verificar si hay clases para hoy o mañana
        if (seEjecuto == false){
            if (clasesHoy.isEmpty() || clasesMañana.isEmpty()) {
                for (HorarioiDiaxTipoClase horarios : confActual.getHorarioiDiaxTipoClaseList()) {

                    Date fechaClase = proximaFecha(horarios.getDia().getNombreDia());

                    Clase clase = new Clase();
                    clase.setDiaClase(horarios.getDia());
                    clase.setFechaHoraClase(fechaClase);
                    clase.setTipoClase(horarios.getTipoClase());
                    List<Profesor> profesores = new ArrayList<>(claseProfesores(horarios.getTipoClase()));
                    clase.setProfesores(profesores);

                    claseRepository.save(clase);
                    claseAlumnoService.generarClaseAlumno(clase.getNroClase());
                }
            }
        }

    }

    // Método para obtener la próxima fecha de un día específico de la semana
    private Date proximaFecha(String diaSemana) {
        DayOfWeek diaBuscado = mapearDiaEspanol(diaSemana.toUpperCase(Locale.ROOT));
        LocalDate hoy = LocalDate.now();
        LocalDate proxima = hoy.with(TemporalAdjusters.nextOrSame(diaBuscado));
        return Date.from(proxima.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    // Sobrecarga para cuando ya tenés DayOfWeek
    private Date proximaFecha(DayOfWeek diaBuscado) {
        LocalDate hoy = LocalDate.now();
        LocalDate proxima = hoy.with(TemporalAdjusters.nextOrSame(diaBuscado));
        return Date.from(proxima.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    // Mapea días en español a DayOfWeek
    private DayOfWeek mapearDiaEspanol(String diaSemana) {
        return switch (diaSemana) {
            case "LUNES" -> DayOfWeek.MONDAY;
            case "MARTES" -> DayOfWeek.TUESDAY;
            case "MIERCOLES" -> DayOfWeek.WEDNESDAY;
            case "JUEVES" -> DayOfWeek.THURSDAY;
            case "VIERNES" -> DayOfWeek.FRIDAY;
            case "SABADO" -> DayOfWeek.SATURDAY;
            case "DOMINGO" -> DayOfWeek.SUNDAY;
            default -> throw new IllegalArgumentException("Día inválido: " + diaSemana);
        };
    }

    //Metodo para traer los profesores de una clase
    private List<Profesor> claseProfesores(TipoClase tipoClase){
        List<InscripcionProfesor> inscripciones = inscripcionProfesorRepository.findByTipoClaseAndFechaBajaInscripcionProfesorIsNull(tipoClase);
        List<Profesor> profesores = new ArrayList<>();
        for (InscripcionProfesor inscripcion : inscripciones) {
            profesores.add(inscripcion.getProfesor());
        }
        return profesores;
    }

    public List<ClaseDTO> getClases() {
        // Traer la clase de hoy y las futuras
        List<Clase> clases = claseRepository.findByFechaBajaClaseIsNullAndFechaHoraClaseAfter(new Date());
        // Verificar que no esté vacío
        if (clases.isEmpty()) {
            throw new IllegalArgumentException("No existen clases registradas");
        }
        // Convertir a DTO
        List<ClaseDTO> claseDTOS = new ArrayList<>();
        for (Clase clase : clases) {
            ClaseDTO claseDTO = new ClaseDTO();
            claseDTO.setNroClase(clase.getNroClase());
            claseDTO.setFechaBajaClase(clase.getFechaBajaClase());
            claseDTO.setFechaHoraClase(clase.getFechaHoraClase());
            List<ProfesorDto> profesorDTOS = new ArrayList<>();
            for (Profesor profesor : clase.getProfesores()) {
                ProfesorDto profesorDTO = new ProfesorDto();
                profesorDTO.setNroProfesor(profesor.getNroProfesor());
                profesorDTO.setDniProfesor(profesor.getDniProfesor());
                profesorDTO.setNombreProfesor(profesor.getNombreProfesor());
                profesorDTO.setTelefonoProfesor(profesor.getTelefonoProfesor());
                profesorDTO.setFechaBajaProfesor(profesor.getFechaBajaProfesor());
                profesorDTO.setEmailProfesor(profesor.getEmailProfesor());
                profesorDTOS.add(profesorDTO);
            }
            claseDTO.setProfesores(profesorDTOS);
            // TipoClase
            TipoClase tipoClase = clase.getTipoClase();
            TipoClaseDTO tipoClaseDT = new TipoClaseDTO();
            tipoClaseDT.setCodTipoClase(tipoClase.getCodTipoClase());
            tipoClaseDT.setNombreTipoClase(tipoClase.getNombreTipoClase());
            tipoClaseDT.setCupoMaxTipoClase(tipoClase.getCupoMaxTipoClase());
            tipoClaseDT.setFechaBajaTipoClase(tipoClase.getFechaBajaTipoClase());
            claseDTO.setTipoClase(tipoClaseDT);
            // Dia
            Dia dia = clase.getDiaClase();
            org.example.dto.DiaDTO diaDTO = new org.example.dto.DiaDTO();
            diaDTO.setCodDia(dia.getCodDia());
            diaDTO.setNombreDia(dia.getNombreDia());
            claseDTO.setDiaClase(diaDTO);
            claseDTOS.add(claseDTO);
        }
        return claseDTOS;

    }


}

