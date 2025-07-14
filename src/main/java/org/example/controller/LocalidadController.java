package org.example.controller;


import org.example.entity.Localidad;
import org.example.services.LocalidadServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/localidades")
@CrossOrigin(origins = "*")
public class LocalidadController {
}
