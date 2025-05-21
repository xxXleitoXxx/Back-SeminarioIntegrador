package org.example.services;

import org.example.entity.Alumno;
import org.example.repository.AlumnoRepository;
import org.example.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlumnoServiceImp extends BaseServiceImpl<Alumno,Long> implements AlumnoService{
    @Autowired
    AlumnoRepository alumnoRepository;

    public AlumnoServiceImp(BaseRepository<Alumno, Long> BaseRespository , AlumnoRepository alumnoRepository) {
        super(BaseRespository);
        this.alumnoRepository = alumnoRepository;
    }
}
