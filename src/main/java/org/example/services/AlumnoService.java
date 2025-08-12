package org.example.services;


import org.example.dto.AlumnoDto;
import org.example.dto.ContactoEmergenciaDTO;
import org.example.dto.FichaMedicaDTO;
import org.example.entity.Alumno;
import org.example.entity.ContactoEmergencia;
import org.example.entity.FichaMedica;
import org.example.entity.Localidad;
import org.example.repository.AlumnoRepository;
import org.example.repository.LocalidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AlumnoService {
    @Autowired
    AlumnoRepository alumnoRepository;
    @Autowired
    private LocalidadService localidadService;
    @Autowired
    private FichaMedicaServices FichaMedicaService;

    // Método para crear un nuevo Alumno
    public Alumno crearAlumno(AlumnoDto nuevoAlumno) {
        //validamos que el alumno no exista en la base de datos

        existeAlumnoPorDni(nuevoAlumno.getDniAlumno());

        Alumno alumno = new Alumno();
        alumno.setNombreAlumno(nuevoAlumno.getNombreAlumno());
        alumno.setApellidoAlumno(nuevoAlumno.getApellidoAlumno());
        alumno.setFechaNacAlumno(nuevoAlumno.getFechaNacAlumno());
        alumno.setDniAlumno(nuevoAlumno.getDniAlumno());
        alumno.setMailAlumno(nuevoAlumno.getMailAlumno());
        alumno.setTelefonoAlumno(nuevoAlumno.getTelefonoAlumno());
        alumno.setDomicilioAlumno(nuevoAlumno.getDomicilioAlumno());

        //comprobamos que la localidad no sea nula
        Localidad localidad = localidadService.buscarLocalidadPorCodigoYFechaBaja(nuevoAlumno.getLocalidadAlumno().getCodLocalidad());
        alumno.setLocalidad(localidad); // Asignamos la localidad al Alumno

        //comprobamos que la ficha medica no sea nula
        if (nuevoAlumno.getFichaMedicaDTO() == null) {
            throw new IllegalArgumentException("La ficha medica debe ser cargada");
        }//Creamos una nueva FichaMedica y la asociamos al Alumno
        /*
        List <FichaMedica> fichasMedicas= new ArrayList<>();
        FichaMedica fichaMedica = new FichaMedica();
        fichasMedicas.add(fichaMedica);
        fichaMedica.setFechaBajaFichaMedica(null); // Asignamos la fecha de baja como nula al crear
        fichaMedica.setArchivo(nuevoAlumno.getFichaMedicaDTO().getArchivo());
        alumno.setFichasMedicas(fichasMedicas);

         */
        // Asignamos los contactos de emergencia del DTO al Alumno
        //comprobamos que el contacto de emergencia no sea nulo
        if (nuevoAlumno.getContactoEmergenciaDTO() == null || nuevoAlumno.getContactoEmergenciaDTO().isEmpty()) {
            throw new IllegalArgumentException("Debe agregar al menos un contacto de emergencia");
        }

        // Aquí asumimos que ContactoEmergenciaDTO tiene los campos necesarios para crear ContactoEmergencia
        List<ContactoEmergencia> contactoEmergencia = new ArrayList<>();
        List<ContactoEmergenciaDTO> nuevoContactoEmergencia = nuevoAlumno.getContactoEmergenciaDTO();
        for (ContactoEmergenciaDTO contactoDTO : nuevoContactoEmergencia) {
            ContactoEmergencia contacto = getContactoEmergencia(contactoDTO);
            // Setea aquí los demás campos necesarios
            contactoEmergencia.add(contacto);
        }
        alumno.setContactosEmergencia(contactoEmergencia);
        // Aquí puedes agregar más campos según tu entidad Alumno
        // Si tienes relaciones con otras entidades, asegúrate de establecerlas correctamente
        // Por ejemplo, si tienes una relación con Localidad, Provincia o Pais, debes establecerlas aquí

        return alumnoRepository.save(alumno); // Guarda el nuevo Alumno en la base de datos
    }

    private static ContactoEmergencia getContactoEmergencia(ContactoEmergenciaDTO contactoDTO) {
        ContactoEmergencia contacto = new ContactoEmergencia();
        //Comprobamos que los atributos del contacto no sean nulos
        if (contactoDTO.getNombreContacto() == null) {
            throw new IllegalArgumentException("El nombre del contacto no puede ser nulo");
        }
        //comprobamos que el telefono del contacto no sea nulo
        if (contactoDTO.getTelefonoContacto() == null) {
            throw new IllegalArgumentException("El telefono del contacto no puede ser nulo");
        }
        contacto.setNombreContacto(contactoDTO.getNombreContacto());
        contacto.setTelefonoContacto(contactoDTO.getTelefonoContacto());
        return contacto;
    }

    //Metodo para validar si un alumno existe por su DNI
    public void existeAlumnoPorDni(int dniAlumno) {
        if (alumnoRepository.findByDniAlumno(dniAlumno).isPresent()) {
            ;
            throw new IllegalArgumentException("Ya existe un alumno con el DNI proporcionado");
        }
    }

    //Añadir otro contacto de Emergencia a un Alumno
    public ContactoEmergencia agregarContactoEmergencia(Long alumnoId, ContactoEmergencia nuevoContacto) {
        Alumno alumno = alumnoRepository.findById(alumnoId).orElseThrow(() -> new NoSuchElementException("Alumno no encontrado"));

        alumno.getContactosEmergencia().add(nuevoContacto);
        alumnoRepository.save(alumno); // CascadeType.ALL guarda el contacto también

        return nuevoContacto;
    }

    public List<Alumno> getAlumnos() {
        List<Alumno> alumnos = alumnoRepository.findAll();
        return alumnos;
    }

    public Alumno getAlumnoByNroAlumno(Long nroAlumno) {
        return alumnoRepository.findById(nroAlumno).orElseThrow(() -> new NoSuchElementException("Alumno no encontrado con nroAlumno: " + nroAlumno));
    }
}
