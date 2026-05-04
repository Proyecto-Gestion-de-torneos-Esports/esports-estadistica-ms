package Torneo.Estadistica.repository;

import Torneo.Estadistica.model.Estadistica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EstadisticaRepository extends JpaRepository<Estadistica, Long> {
    //busca todas las estadistiscas de un jugador
    List<Estadistica> findByJugadorId(long jugadorId);
    // buscar todas estadisticas de una partida en concreto
    List<Estadistica> findByPartidaId(Long partidaId);


    // query busca una metrica especifica de un jugador en una partida
    @Query("SELECT e FROM Estadistica  e WHERE  e.usuarioId = :jugadorId AND e.usuarioId = :partidaId")
    List<Estadistica> findEstadisticaByJugadorIdAndMetrica(@Param("playerId") Long playerId, @Param("partidaId") Long partidaId);

}
