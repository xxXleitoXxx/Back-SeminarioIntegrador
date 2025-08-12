package org.example.services;

import org.example.entity.Alumno;
import org.example.entity.ContactoEmergencia;
import org.example.repository.AlumnoRepository;
import org.example.repository.ContactoEmergenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ContactoEmergenciaService {
    @Autowired
    ContactoEmergenciaRepository contactoEmergenciaRepository;
@Autowired
    AlumnoRepository alumnoRepository;
    public List<ContactoEmergencia> getContactosEmergenciaPorAlumno(Long nroAlumno) {
Alumno alumno = alumnoRepository.findById(nroAlumno).orElseThrow(() -> new NoSuchElementException("Alumno no encontrado con nroAlumno: " + nroAlumno));
List<ContactoEmergencia> contactosEmergencia = alumno.getContactosEmergencia();

return contactosEmergencia;
    }


}
