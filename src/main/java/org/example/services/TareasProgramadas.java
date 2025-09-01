package org.example.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TareasProgramadas {
    @Autowired
    private  ClaseService claseService;

    // Tarea programada para ejecutar el método generarclases() todos los días a las 00:00
    @Scheduled(cron = "0 0 0 * * ?")
    public void ejecutarGenerarClasesHoy() {
        claseService.generarclases();
    }

    // Tarea programada para ejecutar el método generarclases() cada 60 segundos
//    @Scheduled(fixedRate = 60000)
//    public void ejecutarGenerarClasesCadaMinuto() {
//        claseService.generarclases();
//    }
}
