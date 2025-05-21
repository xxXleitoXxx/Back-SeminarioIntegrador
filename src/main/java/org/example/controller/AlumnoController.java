package org.example.controller;

import org.example.entity.Alumno;
import org.example.services.AlumnoServiceImp;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/Alumnos")
public class AlumnoController extends BaseControllerImpl<Alumno, AlumnoServiceImp> {
}