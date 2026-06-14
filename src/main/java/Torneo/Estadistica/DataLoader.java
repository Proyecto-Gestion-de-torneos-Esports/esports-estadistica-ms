package Torneo.Estadistica;

import Torneo.Estadistica.model.Estadistica;
import Torneo.Estadistica.repository.EstadisticaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import net.datafaker.Faker;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class DataLoader implements CommandLineRunner {
    private final EstadisticaRepository repository;

    public DataLoader(EstadisticaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            Faker faker = new Faker(new Locale("es"));
            List<Estadistica> estadisticas = new ArrayList<>();

            String[] metricas = {"Kills", "Muertes", "Asistencias", "Goles", "Daño Infligido", "Headshots", "Oro Obtenido"};

            for (int i = 0; i < 30; i++) {
                Estadistica est = new Estadistica();
                est.setUsuarioId(faker.number().numberBetween(1L, 100L));
                est.setPartidasTorneoId(faker.number().numberBetween(1L, 50L));
                est.setMetrica(metricas[faker.random().nextInt(metricas.length)]);

                int valorAleatorio = est.getMetrica().equals("Daño Infligido") || est.getMetrica().equals("Oro Obtenido")
                        ? faker.number().numberBetween(1000, 25000)
                        : faker.number().numberBetween(0, 30);

                est.setValor(valorAleatorio);
                est.setActivo(true);

                estadisticas.add(est);
            }
            repository.saveAll(estadisticas);
            System.out.println("30 registros generados exitosamente");
        }
    }
}
