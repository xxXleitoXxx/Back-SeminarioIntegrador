package org.example.services;


import org.example.dto.RangoEtarioDTO;

import org.example.entity.RangoEtario;
import org.example.repository.RangoEtarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RangoEtarioService {
    @Autowired
    RangoEtarioRepository rangoEtarioRepository;

    //metodo para crear un nuevo rango etario
    public RangoEtario crearRangoEtario(RangoEtarioDTO nuevorangoetario){


        RangoEtario rangoetario= new RangoEtario();
        rangoetario.setEdadDesde(nuevorangoetario.getEdadDesde());
        rangoetario.setEdadHasta(nuevorangoetario.getEdadHasta());
        return rangoEtarioRepository.save(rangoetario);

    }
}
