package org.example.services;


import org.example.dto.RangoEtarioDTO;

import org.example.entity.Profesor;
import org.example.entity.RangoEtario;
import org.example.entity.TipoClase;
import org.example.repository.RangoEtarioRepository;
import org.example.repository.TipoClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RangoEtarioService {
    @Autowired
    RangoEtarioRepository rangoEtarioRepository;
    @Autowired
    TipoClaseRepository tipoClaseRepository;

    //metodo para crear un nuevo rango etario
    public RangoEtarioDTO crearRangoEtario(RangoEtarioDTO nuevorangoetario){

        //verificar que el codigo no este en uso
        existeRangoEtario(nuevorangoetario.getCodRangoEtario());

        //verificar que no haya un rango etario existente con esos rangos
//        existeRango(nuevorangoetario.getEdadDesde(), nuevorangoetario.getEdadHasta());

        //verificar que edad desde sea menor a edad hasta
        verificarEdades(nuevorangoetario);
        //verificar que no exista un rango etario con ese nombre
        rangoEtarioRepository.findByNombreRangoEtarioAndFechaBajaRangoEtarioIsNull(nuevorangoetario.getNombreRangoEtario())
                .ifPresent(rango -> {
                    throw new IllegalArgumentException("Ya existe un rango etario con ese nombre.");
                });


        //crear rango etario
        RangoEtario rangoetario= new RangoEtario();
        rangoetario.setCodRangoEtario(nuevorangoetario.getCodRangoEtario());
        rangoetario.setEdadDesde(nuevorangoetario.getEdadDesde());
        rangoetario.setEdadHasta(nuevorangoetario.getEdadHasta());
        rangoetario.setFechaBajaRangoEtario(nuevorangoetario.getFechaBajaRangoEtario());
        rangoetario.setNombreRangoEtario(nuevorangoetario.getNombreRangoEtario());
        rangoEtarioRepository.save(rangoetario);
        RangoEtarioDTO rangoEtarioDTO = new RangoEtarioDTO();
        rangoEtarioDTO.setCodRangoEtario(rangoetario.getCodRangoEtario());
        rangoEtarioDTO.setEdadDesde(rangoetario.getEdadDesde());
        rangoEtarioDTO.setEdadHasta(rangoetario.getEdadHasta());
        rangoEtarioDTO.setFechaBajaRangoEtario(rangoetario.getFechaBajaRangoEtario());
        rangoEtarioDTO.setNombreRangoEtario(rangoetario.getNombreRangoEtario());
        return rangoEtarioDTO;

    }

    //verificar que edad desde sea menor a edad hasta
    public void verificarEdades(RangoEtarioDTO rangoEtarioDTO){
        if (rangoEtarioDTO.getEdadDesde() >= rangoEtarioDTO.getEdadHasta()){
            throw new IllegalArgumentException("La edad desde debe ser menor a la edad hasta");
        }
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
        //verificar que exista y no este dado de baja
        RangoEtario rangoEtario = rangoEtarioRepository.findBycodRangoEtario(codRangoEtario).orElseThrow(() -> new IllegalArgumentException("No existe un rango etario   con el codigo " + codRangoEtario));
        if (rangoEtario.getFechaBajaRangoEtario() != null){
            throw new IllegalStateException("No se puede modificar un rango etario dado de baja");
        }

        //verificar que no este relacionado con un tipo de clase
        verificarRelacionConTipoClase(rangoEtario);
        //verificar que edad desde sea menor a edad hasta
        verificarEdades(rangoEtarioDTO);

//        //verificar que no haya un rango etario existente con esos rangos
//        existeRango(rangoEtarioDTO.getEdadDesde(), rangoEtarioDTO.getEdadHasta());
        rangoEtario.setNombreRangoEtario(rangoEtario.getNombreRangoEtario());
        rangoEtario.setEdadHasta(rangoEtarioDTO.getEdadHasta());
        rangoEtario.setEdadDesde(rangoEtarioDTO.getEdadDesde());
        return rangoEtarioRepository.save(rangoEtario);

    }
    //verificar que no este relacionado con un tipo de clase
    public void verificarRelacionConTipoClase(RangoEtario rangoEtario){
        List<TipoClase> tipoClases = tipoClaseRepository.findByRangoEtarioAndFechaBajaTipoClaseIsNull(rangoEtario);
        if (!tipoClases.isEmpty()){
            throw new IllegalStateException("No se puede modificar un rango etario asociado a un tipo de clase activo");
        }
    }

    //dar de baja rangoetario
    public RangoEtarioDTO  bajaRangoEtario(Long codRangoEtario, Date fechaBajaRE){
        //verificar que exista y no este dado de baja
        RangoEtario rangoEtario = rangoEtarioRepository.findBycodRangoEtario(codRangoEtario).orElseThrow(() -> new IllegalArgumentException("No existe un rango etario   con el codigo " + codRangoEtario));
        if (rangoEtario.getFechaBajaRangoEtario() != null){
            throw new IllegalStateException("No se puede dar de baja un rango etario dado de baja");
        }
        //verificar que no este relacionado con un tipo de clase
        verificarRelacionConTipoClase(rangoEtario);

        //dar de baja
        rangoEtario.setFechaBajaRangoEtario(fechaBajaRE);
        rangoEtarioRepository.save(rangoEtario);
        RangoEtarioDTO rangoEtarioDTO = new RangoEtarioDTO();
        rangoEtarioDTO.setCodRangoEtario(rangoEtario.getCodRangoEtario());
        rangoEtarioDTO.setEdadDesde(rangoEtario.getEdadDesde());
        rangoEtarioDTO.setEdadHasta(rangoEtario.getEdadHasta());
        rangoEtarioDTO.setFechaBajaRangoEtario(rangoEtario.getFechaBajaRangoEtario());
        rangoEtarioDTO.setNombreRangoEtario(rangoEtario.getNombreRangoEtario());
        return rangoEtarioDTO;
    }

    //Traer todos los rangos etarios
    public List<RangoEtarioDTO> getRangoEtarios() {
        //traer todos los rangos etarios
        List<RangoEtario> rangosEtarios = rangoEtarioRepository.findAll();

        //verificar que no este vacio
        if (rangosEtarios.isEmpty()) {
            throw new IllegalArgumentException("No existen rangos etarios registrados");
        }

        //convertir a dto
        List<RangoEtarioDTO> rangoEtarioDtos = new ArrayList<>();
        for (RangoEtario  rangoEtario : rangosEtarios) {
            RangoEtarioDTO rangoEtarioDTO = new RangoEtarioDTO();
            rangoEtarioDTO.setCodRangoEtario(rangoEtario.getCodRangoEtario());
            rangoEtarioDTO.setEdadDesde(rangoEtario.getEdadDesde());
            rangoEtarioDTO.setEdadHasta(rangoEtario.getEdadHasta());
            rangoEtarioDTO.setFechaBajaRangoEtario(rangoEtario.getFechaBajaRangoEtario());
            rangoEtarioDTO.setNombreRangoEtario(rangoEtario.getNombreRangoEtario());
            rangoEtarioDtos.add(rangoEtarioDTO);
        }

        return rangoEtarioDtos;
    }



}
