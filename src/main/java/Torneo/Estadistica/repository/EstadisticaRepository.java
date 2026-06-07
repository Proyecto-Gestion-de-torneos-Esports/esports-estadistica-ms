package Torneo.Estadistica.repository;

import Torneo.Estadistica.model.Estadistica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EstadisticaRepository extends JpaRepository<Estadistica, Long> {
    //busca todas las estadistiscas de un usuario
    List<Estadistica> findByUsuarioId(Long usuarioId);
    // buscar todas estadisticas de una partida en concreto
    List<Estadistica> findByPartidasTorneoId(Long partidasTorneoId);

    @Query("SELECT e FROM Estadistica e WHERE e.usuarioId = :usuarioId AND e.partidasTorneoId = :partidasTorneoId AND e.metrica = :metrica")
    List<Estadistica> findMetricaEspecifica(
            @Param("usuarioId") Long usuarioId,
            @Param("partidasTorneoId") Long partidasTorneoId,
            @Param("metrica") String metrica
    );
}
