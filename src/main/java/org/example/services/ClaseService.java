package org.example.services;

import org.example.entity.Clase;
import org.example.entity.ConfHorarioTipoClase;
import org.example.entity.HorarioiDiaxTipoClase;
import org.example.entity.Profesor;
import org.example.repository.ClaseRepository;
import org.example.repository.ConfHorarioTipoClaseRepository;
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

    public void generarclases() {
        // Buscar configuración activa
        ConfHorarioTipoClase confActual = confHorarioTipoClaseRepository
                .findByFechaFinVigenciaConfIsNull()
                .orElseThrow(() -> new IllegalArgumentException("No hay una configuración activa de horarios"));

        LocalDate hoy = LocalDate.now();
        LocalDate vigencia = confActual.getFechaVigenciaConf().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
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

                List<Profesor> profesores = new ArrayList<>(horarios.getTipoClase().getProfesores());
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

        if (seEjecuto == false){
            if (clasesHoy.isEmpty() || clasesMañana.isEmpty()) {
                for (HorarioiDiaxTipoClase horarios : confActual.getHorarioiDiaxTipoClaseList()) {

                    Date fechaClase = proximaFecha(horarios.getDia().getNombreDia());

                    Clase clase = new Clase();
                    clase.setDiaClase(horarios.getDia());
                    clase.setFechaHoraClase(fechaClase);
                    clase.setTipoClase(horarios.getTipoClase());

                    List<Profesor> profesores = new ArrayList<>(horarios.getTipoClase().getProfesores());
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







}

