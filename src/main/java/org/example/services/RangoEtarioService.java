package org.example.services;


import org.example.dto.RangoEtarioDTO;

import org.example.entity.Profesor;
import org.example.entity.RangoEtario;
import org.example.repository.RangoEtarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RangoEtarioService {
    @Autowired
    RangoEtarioRepository rangoEtarioRepository;

    //metodo para crear un nuevo rango etario
    public RangoEtario crearRangoEtario(RangoEtarioDTO nuevorangoetario){

        //verificar que el codigo no este en uso
        existeRangoEtario(nuevorangoetario.getCodRangoEtario());

        //verificar que no haya un rango etario existente con esos rangos
        existeRango(nuevorangoetario.getEdadDesde(), nuevorangoetario.getEdadHasta());

        RangoEtario rangoetario= new RangoEtario();
        rangoetario.setCodRangoEtario(nuevorangoetario.getCodRangoEtario());
        rangoetario.setEdadDesde(nuevorangoetario.getEdadDesde());
        rangoetario.setEdadHasta(nuevorangoetario.getEdadHasta());
        rangoetario.setFechaBajaRangoEtario(nuevorangoetario.getFechaBajaRangoEtario());


        return rangoEtarioRepository.save(rangoetario);

    }

    public void existeRangoEtario(Long codRangoEtario) {
        rangoEtarioRepository.findBycodRangoEtario(codRangoEtario)
                .ifPresent(rangoEtario -> {
                    if (rangoEtario.getFechaBajaRangoEtario() == null) {
                        throw new IllegalArgumentException("Ya existe un rango etario activo con el codigo proporcionado");
                    }
                });
    }

    public void existeRango(int edadDesde, int edadHasta){
        rangoEtarioRepository.findByEdadDesdeAndEdadHasta(edadDesde,edadHasta).ifPresent(rangoEtario -> {
            throw new IllegalArgumentException("Ya existe un rango etario activo con el rango proporcionado");
        });

    }

    //modificar rangoetario
    public RangoEtario modificarRangoEtario(Long codRangoEtario,  RangoEtarioDTO rangoEtarioDTO){
        RangoEtario rangoEtario = rangoEtarioRepository.findBycodRangoEtario(codRangoEtario).orElseThrow(() -> new IllegalArgumentException("No existe un rango etario   con el codigo " + codRangoEtario));
        if (rangoEtario.getFechaBajaRangoEtario() != null){
            throw new IllegalStateException("No se puede modificar un rango etario dado de baja");
        }

        //verificar que no haya un rango etario existente con esos rangos
        existeRango(rangoEtarioDTO.getEdadDesde(), rangoEtarioDTO.getEdadHasta());

        rangoEtario.setEdadHasta(rangoEtarioDTO.getEdadHasta());
        rangoEtario.setEdadDesde(rangoEtarioDTO.getEdadDesde());
        return rangoEtarioRepository.save(rangoEtario);

    }

    //dar de baja rangoetario
    public RangoEtario  bajaRangoEtario(Long codRangoEtario, Date fechaBajaRE){
        RangoEtario rangoEtario = rangoEtarioRepository.findBycodRangoEtario(codRangoEtario).orElseThrow(() -> new IllegalArgumentException("No existe un rango etario   con el codigo " + codRangoEtario));
        if (rangoEtario.getFechaBajaRangoEtario() != null){
            throw new IllegalStateException("No se puede dar de baja un rango etario dado de baja");
        }

        rangoEtario.setFechaBajaRangoEtario(fechaBajaRE);
        return rangoEtarioRepository.save(rangoEtario);

    }

    //Traer todos los profesores
    public List<RangoEtario> getRangoEtarios() {

        List<RangoEtario> rangosEtarios = rangoEtarioRepository.findAll();
        if (rangosEtarios.isEmpty()) {
            throw new IllegalArgumentException("No existen rangos etarios registrados");
        }
        return rangosEtarios;
    }



}
