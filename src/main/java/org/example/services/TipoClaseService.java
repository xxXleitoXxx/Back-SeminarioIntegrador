package org.example.services;


import org.example.dto.TipoClaseDTO;
import org.example.entity.Profesor;
import org.example.entity.RangoEtario;
import org.example.entity.TipoClase;
import org.example.repository.RangoEtarioRepository;
import org.example.repository.TipoClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TipoClaseService {
    @Autowired
    TipoClaseRepository tipoClaseRepository;
    @Autowired
    RangoEtarioRepository rangoEtarioRepository;
    //metodo crear tipo clase
    public TipoClaseDTO crearTipoClase(TipoClaseDTO nuevoTipoClase){
        //metodo que verifica si ya existe el tipoclase
        validarTipoClaseNoExistente(nuevoTipoClase.getCodTipoClase());

        TipoClase tipoClase = new TipoClase();
        tipoClase.setFechaBajaTipoClase(nuevoTipoClase.getFechaBajaTipoClase());
        tipoClase.setCupoMaxTipoClase(nuevoTipoClase.getCupoMaxTipoClase());
        tipoClase.setNombreTipoClase(nuevoTipoClase.getNombreTipoClase());
        RangoEtario rangoEtario = rangoEtarioRepository.findBycodRangoEtario(nuevoTipoClase.getRangoEtarioDTO().getCodRangoEtario()).orElseThrow();
        tipoClase.setRangoEtario(rangoEtario);
        tipoClaseRepository.save(tipoClase);
        TipoClaseDTO tipoClaseDTO = new TipoClaseDTO();
        tipoClaseDTO.setCodTipoClase(tipoClase.getCodTipoClase());
        tipoClaseDTO.setCupoMaxTipoClase(tipoClase.getCupoMaxTipoClase());
        tipoClaseDTO.setNombreTipoClase(tipoClase.getNombreTipoClase());
        tipoClaseDTO.setRangoEtarioDTO(nuevoTipoClase.getRangoEtarioDTO());
        return tipoClaseDTO;
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
    public TipoClase modificarTipoClase(Long codTipoClase,TipoClaseDTO tipoClaseDTO){
        TipoClase tipoClase = tipoClaseRepository.findBycodTipoClase(codTipoClase).orElseThrow(() -> new IllegalArgumentException("No existe un tipoclase  con el codigo " + codTipoClase));

        //validar que no este dado de baja
        if (tipoClase.getFechaBajaTipoClase() != null){
            throw new IllegalArgumentException("El tipoclase está dado de baja.");
        }
        RangoEtario  rangoEtario = rangoEtarioRepository.findBycodRangoEtario(tipoClaseDTO.getRangoEtarioDTO().getCodRangoEtario()).orElseThrow(() -> new IllegalArgumentException("No existe un rango etario con el codigo " + tipoClaseDTO.getRangoEtarioDTO().getCodRangoEtario()));
        tipoClase.setRangoEtario(rangoEtario);
        tipoClase.setCupoMaxTipoClase(tipoClaseDTO.getCupoMaxTipoClase());
        tipoClase.setNombreTipoClase(tipoClaseDTO.getNombreTipoClase());
        return tipoClaseRepository.save(tipoClase);

    }

    //metodo dar de baja tipo clase
    public TipoClase bajaTipoClase(Long codTipoClase, Date fechaBaja){
        TipoClase tipoClase = tipoClaseRepository.findBycodTipoClase(codTipoClase).orElseThrow(() -> new IllegalArgumentException("No existe un tipoclase  con el codigo " + codTipoClase));

        //validar que no este dado de baja
        if (tipoClase.getFechaBajaTipoClase() != null){
            throw new IllegalArgumentException("El tipoclase ya está dado de baja.");
        }

        tipoClase.setFechaBajaTipoClase(fechaBaja);
        return tipoClaseRepository.save(tipoClase);

    }

    //traer todos los tipoclase
    public List<TipoClase> getTipoClases() {

        List<TipoClase> tipoClases = tipoClaseRepository.findAll();
        if (tipoClases.isEmpty()) {
            throw new IllegalArgumentException("No existen tipo clases registrados");
        }
        return tipoClases;
    }




}
