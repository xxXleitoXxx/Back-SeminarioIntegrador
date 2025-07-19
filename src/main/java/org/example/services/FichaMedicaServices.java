package org.example.services;

import org.example.dto.FichaMedicaDTO;
import org.springframework.stereotype.Service;

@Service
public class FichaMedicaServices {

    //Metodo Para Agregar una ficha medica a un Alumno
    public void agregarFichaMedica(Long AlumnoId,FichaMedicaDTO fichaMedicaDTO) {
        // Lógica para agregar la ficha médica
        // Aquí se podría llamar a un repositorio para guardar la ficha médica en la base de datos
    }
}
