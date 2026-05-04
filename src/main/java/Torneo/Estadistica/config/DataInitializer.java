package Torneo.Estadistica.config;

import Torneo.Estadistica.model.Estadistica;
import Torneo.Estadistica.repository.EstadisticaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final EstadisticaRepository estadisticaRepository;

    @Override
    public void run(String... args) {
        //si hay datos en la tabla, se detiene la ejecucion para no duplicar
        if (estadisticaRepository.count() > 0){
            log.info(">>> DataInitializer: La base de datos ya contiene  estadistica . Se omite la carga inicial");
            return;
        }
        log.info("DataInitializer: BD vacia detectada. insertando estadisticas de Esports");
            //shooter
        estadisticaRepository.save(new Estadistica(null, 101L, 500L, "KILLS", 24));
        estadisticaRepository.save(new Estadistica(null, 101L, 500L, "asistencias",5 ));
        estadisticaRepository.save(new Estadistica(null, 102L, 500L, "KILLS", 18));
            //deportivo
        estadisticaRepository.save(new Estadistica(null, 205L, 850L, "GOLES", 3));
        estadisticaRepository.save(new Estadistica(null, 205L, 850L, "TIROS_AL_ARCO", 7));
        estadisticaRepository.save(new Estadistica(null, 208L, 850L, "ATAJADAS", 5));

        log.info("DataInitializer: {} estadisticas insertadas correctamente ", estadisticaRepository.count());
    }
}
