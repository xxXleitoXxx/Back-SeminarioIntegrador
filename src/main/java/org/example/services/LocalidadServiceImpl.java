package org.example.services;

import org.example.entity.Localidad;
import org.example.repository.BaseRepository;
import org.example.repository.LocalidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocalidadServiceImpl extends BaseServiceImpl<Localidad,Long> implements LocalidadService{
    @Autowired
    LocalidadRepository  localidadRepository;

    public LocalidadServiceImpl(BaseRepository<Localidad, Long> baseRespository , LocalidadRepository  localidadRepository) {
        super(baseRespository);
        this.localidadRepository = localidadRepository;
    }
}
