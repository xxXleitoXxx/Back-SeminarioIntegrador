package org.example.services;

import org.example.dto.ClaseDTO;
import org.example.dto.DiaDTO;
import org.example.dto.TipoClaseDTO;
import org.example.entity.*;
import org.example.repository.ClaseRepository;
import org.example.repository.ConfHorarioTipoClaseRepository;
import org.example.repository.InscripcionProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    private List<Profesor> claseProfesores(TipoClase tipoClase){
        List<InscripcionProfesor> inscripciones = inscripcionProfesorRepository.findByTipoClaseAndFechaBajaInscripcionProfesorIsNull(tipoClase);
        List<Profesor> profesores = new ArrayList<>();
        for (InscripcionProfesor inscripcion : inscripciones) {
            profesores.add(inscripcion.getProfesor());
        }
        return profesores;
    }
    private List<ClaseDTO> getClases (){
List<ClaseDTO> claseDTOList =new ArrayList<>();
        for(Clase clase:claseRepository.findByFechaBajaClaseIsNull()){
        ClaseDTO claseDTO =new ClaseDTO();
        claseDTO.setNroClase(clase.getNroClase());
        claseDTO.setFechaHoraClase(clase.getFechaHoraClase());
            DiaDTO diaDTO =new DiaDTO();
            diaDTO.setCodDia(clase.getDiaClase().getCodDia());
            diaDTO.setNombreDia(clase.getDiaClase().getNombreDia());
            claseDTO.setDiaClase(diaDTO);
            TipoClaseDTO tipoClaseDTO =new TipoClaseDTO();
            tipoClaseDTO.setCodTipoClase(clase.getTipoClase().getCodTipoClase());
            tipoClaseDTO.setNombreTipoClase(clase.getTipoClase().getNombreTipoClase());
            claseDTO.setTipoClase(tipoClaseDTO);
        }

return claseDTOList;
    }







}

