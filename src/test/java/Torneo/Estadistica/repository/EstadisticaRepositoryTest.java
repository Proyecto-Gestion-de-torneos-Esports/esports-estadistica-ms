package Torneo.Estadistica.repository;


import Torneo.Estadistica.model.Estadistica;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class EstadisticaRepositoryTest {
    @Autowired
    private EstadisticaRepository estadisticaRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Query: findMetricaEspecifica - Retorna la estadística exacta")
    void testFindMetricaEspecifica() {

        Estadistica est1 = new Estadistica(null, 5L, 10L, "Kills", 20, true); // La que buscamos
        Estadistica est2 = new Estadistica(null, 5L, 10L, "Asistencias", 5, true); // Misma partida/usuario, distinta métrica
        Estadistica est3 = new Estadistica(null, 8L, 10L, "Kills", 12, true); // Misma métrica/partida, distinto usuario

        entityManager.persist(est1);
        entityManager.persist(est2);
        entityManager.persist(est3);
        entityManager.flush();


        List<Estadistica> resultados = estadisticaRepository.findMetricaEspecifica(5L, 10L, "Kills");


        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals("Kills", resultados.get(0).getMetrica());
        assertEquals(5L, resultados.get(0).getUsuarioId());
        assertEquals(10L, resultados.get(0).getPartidasTorneoId());
    }

    @Test
    @DisplayName("Método: findByUsuarioId - Retorna lista de un jugador específico")
    void testFindByUsuarioId() {
        Estadistica est1 = new Estadistica(null, 7L, 10L, "Muertes", 5, true);
        Estadistica est2 = new Estadistica(null, 7L, 11L, "Asistencias", 8, true);
        Estadistica est3 = new Estadistica(null, 9L, 10L, "Kills", 10, true); // De otro jugador

        entityManager.persist(est1);
        entityManager.persist(est2);
        entityManager.persist(est3);
        entityManager.flush();

        List<Estadistica> resultados = estadisticaRepository.findByUsuarioId(7L);

        assertNotNull(resultados);
        assertEquals(2, resultados.size());
    }

    @Test
    @DisplayName("Método: findByPartidasTorneoId - Retorna lista de una partida específica")
    void testFindByPartidasTorneoId() {

        Estadistica est1 = new Estadistica(null, 5L, 25L, "Kills", 15, true);
        Estadistica est2 = new Estadistica(null, 8L, 25L, "Kills", 10, true);
        Estadistica est3 = new Estadistica(null, 5L, 30L, "Kills", 5, true); // De otra partida

        entityManager.persist(est1);
        entityManager.persist(est2);
        entityManager.persist(est3);
        entityManager.flush();

        List<Estadistica> resultados = estadisticaRepository.findByPartidasTorneoId(25L);

        assertNotNull(resultados);
        assertEquals(2, resultados.size());
    }
}

