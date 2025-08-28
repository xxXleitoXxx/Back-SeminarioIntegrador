package org.example.services;


import org.example.dto.AlumnoDto;
import org.example.dto.ContactoEmergenciaDTO;
import org.example.dto.FichaMedicaDTO;
import org.example.dto.LocalidadDto;
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
    @Autowired
    private InscripcionService inscripcionService;

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
        alumno.setLocalidadAlumno(localidad); // Asignamos la localidad al Alumno

// Quitamos la comprobacion porque no hace falta cargarlas de inmediato
//        //comprobamos que la ficha medica no sea nula
//        if (nuevoAlumno.getFichaMedicaDTO() == null) {
//            throw new IllegalArgumentException("La ficha medica debe ser cargada");
//        }
        if (nuevoAlumno.getContactosEmergencia() == null || nuevoAlumno.getContactosEmergencia().isEmpty()) {
            throw new IllegalArgumentException("Debe agregar al menos un contacto de emergencia");
        }

        // Aquí asumimos que ContactoEmergenciaDTO tiene los campos necesarios para crear ContactoEmergencia
        List<ContactoEmergencia> contactoEmergencia = new ArrayList<>();
        List<ContactoEmergenciaDTO> nuevoContactoEmergencia = nuevoAlumno.getContactosEmergencia();
        for (ContactoEmergenciaDTO contactoDTO : nuevoContactoEmergencia) {
            ContactoEmergencia contacto = new ContactoEmergencia();
            contacto.setTelefonoContacto(contactoDTO.getTelefonoContacto());
            contacto.setNombreContacto(contactoDTO.getNombreContacto());
            contacto.setDireccionContacto(contactoDTO.getDireccionContacto()); // Asignamos la dirección del
            // Setea aquí los demás campos necesarios
            contactoEmergencia.add(contacto);
        }
        alumno.setContactosEmergencia(contactoEmergencia);
        // Aquí puedes agregar más campos según tu entidad Alumno
        // Si tienes relaciones con otras entidades, asegúrate de establecerlas correctamente
        // Por ejemplo, si tienes una relación con Localidad, Provincia o Pais, debes establecerlas aquí

        return alumnoRepository.save(alumno); // Guarda el nuevo Alumno en la base de datos
    }

    public AlumnoDto modificarAlumno(AlumnoDto alumnoDto) {
        //validamos que el alumno exista en la base de datos
        Optional<Alumno> alumnoOptional = alumnoRepository.findById(alumnoDto.getNroAlumno());
        if (alumnoOptional.isPresent()) {
            Alumno alumno = alumnoOptional.get();
            // Actualizamos los campos del Alumno
            alumno.setNombreAlumno(alumnoDto.getNombreAlumno());
            alumno.setApellidoAlumno(alumnoDto.getApellidoAlumno());
            alumno.setFechaNacAlumno(alumnoDto.getFechaNacAlumno());
            alumno.setDniAlumno(alumnoDto.getDniAlumno());
            alumno.setMailAlumno(alumnoDto.getMailAlumno());
            alumno.setTelefonoAlumno(alumnoDto.getTelefonoAlumno());
            alumno.setDomicilioAlumno(alumnoDto.getDomicilioAlumno());

            // Actualizamos la localidad del Alumno
            Localidad localidad = localidadService.buscarLocalidadPorCodigoYFechaBaja(alumnoDto.getLocalidadAlumno().getCodLocalidad());
            alumno.setLocalidadAlumno(localidad);
            List<ContactoEmergenciaDTO> contactoDTO = alumnoDto.getContactosEmergencia();
            // Comprobamos que la lista de contactos de emergencia no sea nula o vacía
            if (contactoDTO == null || contactoDTO.isEmpty()) {
                throw new IllegalArgumentException("Debe agregar al menos un contacto de emergencia");
            }
            // Actualizamos los contactos de emergencia del Alumno
            List<ContactoEmergencia> contactosEmergencia = new ArrayList<>();
            for (ContactoEmergenciaDTO contacto : contactoDTO) {
                ContactoEmergencia nuevoContacto = getContactoEmergencia(contacto);
                // Aquí puedes agregar más validaciones si es necesario
                contactosEmergencia.add(nuevoContacto);
            }
            alumno.getContactosEmergencia().clear();
            alumno.getContactosEmergencia().addAll(contactosEmergencia);

            // Guardamos el Alumno modificado en la base de datos
            alumnoRepository.save(alumno);
            return alumnoDto;
        } else {
            throw new NoSuchElementException("No se encontró un Alumno con el ID proporcionado");
        }

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

    //comprueba si el alumno tiene inscripciones activas en clases
    public void tieneInscripcionesActivasPorDni(int dniAlumno) {

        Alumno alumno = alumnoRepository.findByDniAlumno(dniAlumno)
                .orElseThrow(() -> new NoSuchElementException("Alumno no encontrado con DNI: " + dniAlumno));
        inscripcionService.existenInscripciones(alumno);
    }

    //Añadir otro contacto de Emergencia a un Alumno
    public ContactoEmergencia agregarContactoEmergencia(Long alumnoId, ContactoEmergencia nuevoContacto) {
        Alumno alumno = alumnoRepository.findById(alumnoId).orElseThrow(() -> new NoSuchElementException("Alumno no encontrado"));

        alumno.getContactosEmergencia().add(nuevoContacto);
        alumnoRepository.save(alumno); // CascadeType.ALL guarda el contacto también

        return nuevoContacto;
    }

    public List<AlumnoDto> getAlumnos() {
        List<Alumno> alumnos = alumnoRepository.findAll();
        List<AlumnoDto> alumnosDto = new ArrayList<>();
        for (Alumno alumno : alumnos) {
            AlumnoDto alumnoDto = new AlumnoDto();
            alumnoDto.setNroAlumno(alumno.getNroAlumno());
            alumnoDto.setNombreAlumno(alumno.getNombreAlumno());
            alumnoDto.setApellidoAlumno(alumno.getApellidoAlumno());
            alumnoDto.setFechaNacAlumno(alumno.getFechaNacAlumno());
            alumnoDto.setDniAlumno(alumno.getDniAlumno());
            alumnoDto.setMailAlumno(alumno.getMailAlumno());
            alumnoDto.setTelefonoAlumno(alumno.getTelefonoAlumno());
            alumnoDto.setDomicilioAlumno(alumno.getDomicilioAlumno());
            alumnoDto.setFechaBajaAlumno(alumno.getFechaBajaAlumno());

            // Asignamos la localidad al DTO
            if (alumno.getLocalidadAlumno() != null) {
                LocalidadDto localidadDto = new LocalidadDto();
                localidadDto.setCodLocalidad(alumno.getLocalidadAlumno().getCodLocalidad());
                localidadDto.setNombreLocalidad(alumno.getLocalidadAlumno().getNombreLocalidad());
                localidadDto.setFechaBajaLocalidad(alumno.getLocalidadAlumno().getFechaBajaLocalidad());
                alumnoDto.setLocalidadAlumno(localidadDto);
            }
            //Buscamos las fichas medicas del alumno
            List<FichaMedica> fichaMedica = alumno.getFichasMedicas();
            List<FichaMedicaDTO> fichasMedicasDTO = new ArrayList<>();
            for (FichaMedica ficha : fichaMedica) {
                FichaMedicaDTO fichaMedicaDTO = new FichaMedicaDTO();
                fichaMedicaDTO.setId(ficha.getNroFichaMedica());
                fichaMedicaDTO.setVigenciaDesde(ficha.getVigenciaDesde());
                fichaMedicaDTO.setVigenciaHasta(ficha.getVigenciaHasta());
                fichasMedicasDTO.add(fichaMedicaDTO);
            }
            // Asignamos las fichas medicas al DTO
            alumnoDto.setFichaMedicaDTO(fichasMedicasDTO);


            // Asignamos los contactos de emergencia al DTO
            List<ContactoEmergenciaDTO> contactosEmergenciaDTO = new ArrayList<>();
            for (ContactoEmergencia contacto : alumno.getContactosEmergencia()) {
                ContactoEmergenciaDTO contactoDTO = new ContactoEmergenciaDTO();
                contactoDTO.setNombreContacto(contacto.getNombreContacto());
                contactoDTO.setTelefonoContacto(contacto.getTelefonoContacto());
                contactoDTO.setDireccionContacto(contacto.getDireccionContacto());
                contactosEmergenciaDTO.add(contactoDTO);
            }
            alumnoDto.setContactosEmergencia(contactosEmergenciaDTO);

            alumnosDto.add(alumnoDto);
        }
        return alumnosDto;
    }

    public Alumno getAlumnoByNroAlumno(Long nroAlumno) {
        return alumnoRepository.findById(nroAlumno).orElseThrow(() -> new NoSuchElementException("Alumno no encontrado con nroAlumno: " + nroAlumno));
    }

    public Alumno bajaAlumno(int dniAlumno) {
        Alumno alumno = alumnoRepository.findByDniAlumno(dniAlumno)
                .orElseThrow(() -> new NoSuchElementException("Alumno no encontrado con DNI: " + dniAlumno));
        // Verificamos si el alumno ya tiene una fecha de baja
        if (alumno.getFechaBajaAlumno() != null) {
            throw new IllegalArgumentException("El alumno ya está dado de baja");
        }
        //Verificamos si el alumno tiene inscripciones activas en clases
        tieneInscripcionesActivasPorDni(dniAlumno);

        alumno.setFechaBajaAlumno(new java.util.Date()); // Asignamos la fecha de baja actual
        return alumnoRepository.save(alumno); // Guardamos el alumno con la fecha de baja

    }
}
