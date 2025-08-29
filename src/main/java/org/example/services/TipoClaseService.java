package org.example.services;


import org.example.dto.RangoEtarioDTO;
import org.example.dto.TipoClaseDTO;
import org.example.entity.Profesor;
import org.example.entity.RangoEtario;
import org.example.entity.TipoClase;
import org.example.repository.HorarioiDiaxTipoClaseRepository;
import org.example.repository.InscripcionRepository;
import org.example.repository.RangoEtarioRepository;
import org.example.repository.TipoClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TipoClaseService {
    @Autowired
    TipoClaseRepository tipoClaseRepository;
    @Autowired
    RangoEtarioRepository rangoEtarioRepository;
    @Autowired
    InscripcionRepository  inscripcionRepository;
    @Autowired
    HorarioiDiaxTipoClaseRepository horarioiDiaxTipoClaseRepository;
    //metodo crear tipo clase
    public TipoClaseDTO crearTipoClase(TipoClaseDTO nuevoTipoClase){
        //metodo que verifica si ya existe el tipoclase
        validarTipoClaseNoExistente(nuevoTipoClase.getCodTipoClase());

        //verificar que el nombre no este en uso
        tipoClaseRepository.findByNombreTipoClaseAndFechaBajaTipoClaseIsNull(nuevoTipoClase.getNombreTipoClase())
                .ifPresent(tc -> {
                    throw new IllegalArgumentException("Ya existe un TipoClase con ese nombre.");
                });

        //verificar que el cupo maximo sea mayor a 0
        if (nuevoTipoClase.getCupoMaxTipoClase() <= 0){
            throw new IllegalArgumentException("El cupo máximo debe ser mayor a 0.");
        }


        TipoClase tipoClase = new TipoClase();
        tipoClase.setFechaBajaTipoClase(nuevoTipoClase.getFechaBajaTipoClase());
        tipoClase.setCupoMaxTipoClase(nuevoTipoClase.getCupoMaxTipoClase());
        tipoClase.setNombreTipoClase(nuevoTipoClase.getNombreTipoClase());
        RangoEtario rangoEtario = rangoEtarioRepository.findBycodRangoEtario(nuevoTipoClase.getRangoEtarioDTO().getCodRangoEtario()).orElseThrow();
        tipoClase.setRangoEtario(rangoEtario);
        tipoClaseRepository.save(tipoClase);

        return nuevoTipoClase;
    }
    public void validarTipoClaseNoExistente(Long codTipoClase) {
        tipoClaseRepository.findBycodTipoClase(codTipoClase)
                .ifPresent(tc -> {
                    if (tc.getFechaBajaTipoClase() != null){
                        throw new IllegalArgumentException("Ya existe un TipoClase con ese código y no está dado de baja.");
                    }
                });
    }

    //metodo modificar tipoclase
    public TipoClaseDTO modificarTipoClase(Long codTipoClase,TipoClaseDTO tipoClaseDTO){
        TipoClase tipoClase = tipoClaseRepository.findBycodTipoClase(codTipoClase).orElseThrow(() -> new IllegalArgumentException("No existe un tipoclase  con el codigo " + codTipoClase));

        //validar que no este dado de baja
        if (tipoClase.getFechaBajaTipoClase() != null){
            throw new IllegalArgumentException("El tipoclase está dado de baja.");
        }
        //verificar que el nombre no este en uso
        tipoClaseRepository.findByNombreTipoClaseAndFechaBajaTipoClaseIsNull(tipoClaseDTO.getNombreTipoClase())
                .ifPresent(tc -> {
                    if (!tc.getCodTipoClase().equals(codTipoClase)) {
                        throw new IllegalArgumentException("Ya existe un TipoClase con ese nombre.");
                    }
                });
        //verificar que el cupo maximo sea mayor a 0
        if (tipoClaseDTO.getCupoMaxTipoClase() <= 0){
            throw new IllegalArgumentException("El cupo máximo debe ser mayor a 0.");
        }
        //verificar que el cupo maximo no sea menor al cupo actual si es que hay inscripciones

        if (tipoClaseDTO.getCupoMaxTipoClase() < tipoClase.getCupoMaxTipoClase()){
            throw new IllegalArgumentException("No se puede modificar el cupo maximo a un valor menor al cupo actual.");
        }
        //verificar que no haya nadie inscripto en esa clase si se quiere modifcar el rango etario
        if (!inscripcionRepository.findByTipoClaseAndFechaBajaInscripcionIsNull(tipoClase).isEmpty() && !tipoClase.getRangoEtario().getCodRangoEtario().equals(tipoClaseDTO.getRangoEtarioDTO().getCodRangoEtario())){
            throw new IllegalArgumentException("No se puede modificar el rango etario si hay inscripciones activas en esa clase.");
        }

        RangoEtario  rangoEtario = rangoEtarioRepository.findBycodRangoEtario(tipoClaseDTO.getRangoEtarioDTO().getCodRangoEtario()).orElseThrow(() -> new IllegalArgumentException("No existe un rango etario con el codigo " + tipoClaseDTO.getRangoEtarioDTO().getCodRangoEtario()));
        tipoClase.setRangoEtario(rangoEtario);
        tipoClase.setCupoMaxTipoClase(tipoClaseDTO.getCupoMaxTipoClase());
        tipoClase.setNombreTipoClase(tipoClaseDTO.getNombreTipoClase());
        tipoClaseRepository.save(tipoClase);
        TipoClaseDTO   tipoClaseDTO1 = new TipoClaseDTO();
        tipoClaseDTO1.setCodTipoClase(tipoClase.getCodTipoClase());
        tipoClaseDTO1.setNombreTipoClase(tipoClase.getNombreTipoClase());
        tipoClaseDTO1.setCupoMaxTipoClase(tipoClase.getCupoMaxTipoClase());
        tipoClaseDTO1.setRangoEtarioDTO(tipoClaseDTO.getRangoEtarioDTO());
        tipoClaseDTO1.setFechaBajaTipoClase(tipoClase.getFechaBajaTipoClase());
        return tipoClaseDTO1;
    }

    //metodo dar de baja tipo clase
    public TipoClaseDTO bajaTipoClase(Long codTipoClase, Date fechaBaja){
        TipoClase tipoClase = tipoClaseRepository.findBycodTipoClase(codTipoClase).orElseThrow(() -> new IllegalArgumentException("No existe un tipoclase  con el codigo " + codTipoClase));

        //validar que no este dado de baja
        if (tipoClase.getFechaBajaTipoClase() != null){
            throw new IllegalArgumentException("El tipoclase ya está dado de baja.");
        }
        //verificar que no haya nadie inscripto en esa clase si se quiere modifcar el cupo maximo y el rango etario
        if (!inscripcionRepository.findByTipoClaseAndFechaBajaInscripcionIsNull(tipoClase).isEmpty()){
            throw new IllegalArgumentException("No se puede dar de baja si hay inscripciones activas en esa clase.");
        }
        //verificar que no haya un horario asociado a ese tipo de clase de una configuracion vigente
        if (!horarioiDiaxTipoClaseRepository.findByTipoClaseAndFechaBajaHFxTCIsNull(tipoClase).isEmpty()){
            throw new IllegalArgumentException("No se puede dar de baja si hay horarios activos asociados a esa clase.");
        }
        //verificar que no haya un profesor asociado a ese tipo de clase

        tipoClase.setFechaBajaTipoClase(fechaBaja);
        tipoClaseRepository.save(tipoClase);
        TipoClaseDTO  tipoClaseDTO1 = new TipoClaseDTO();
        tipoClaseDTO1.setCodTipoClase(tipoClase.getCodTipoClase());
        tipoClaseDTO1.setNombreTipoClase(tipoClase.getNombreTipoClase());
        tipoClaseDTO1.setCupoMaxTipoClase(tipoClase.getCupoMaxTipoClase());
        tipoClaseDTO1.setFechaBajaTipoClase(tipoClase.getFechaBajaTipoClase());
        return tipoClaseDTO1;

    }

    //traer todos los tipoclase
    public List<TipoClaseDTO> getTipoClases() {

        List<TipoClase> tipoClases = tipoClaseRepository.findAll();
        if (tipoClases.isEmpty()) {
            throw new IllegalArgumentException("No existen tipo clases registrados");
        }
        List<TipoClaseDTO>  tipoClaseDTOS  = new ArrayList<>();
        for (TipoClase tipoClase : tipoClases) {
            TipoClaseDTO tipoClaseDTO = new TipoClaseDTO();
            tipoClaseDTO.setCodTipoClase(tipoClase.getCodTipoClase());
            tipoClaseDTO.setNombreTipoClase(tipoClase.getNombreTipoClase());
            tipoClaseDTO.setCupoMaxTipoClase(tipoClase.getCupoMaxTipoClase());
            tipoClaseDTO.setFechaBajaTipoClase(tipoClase.getFechaBajaTipoClase());
            RangoEtario rangoEtario = tipoClase.getRangoEtario();
            if (rangoEtario != null) {
                RangoEtarioDTO  rangoEtarioDTO = new RangoEtarioDTO();
                rangoEtarioDTO.setCodRangoEtario(rangoEtario.getCodRangoEtario());
                rangoEtarioDTO.setNombreRangoEtario(rangoEtario.getNombreRangoEtario());
                rangoEtarioDTO.setEdadDesde(rangoEtario.getEdadDesde());
                rangoEtarioDTO.setEdadHasta(rangoEtario.getEdadHasta());
                rangoEtarioDTO.setFechaBajaRangoEtario(rangoEtario.getFechaBajaRangoEtario());
                tipoClaseDTO.setRangoEtarioDTO(rangoEtarioDTO);
            }
            tipoClaseDTOS.add(tipoClaseDTO);
        }
        return tipoClaseDTOS;
    }




}
