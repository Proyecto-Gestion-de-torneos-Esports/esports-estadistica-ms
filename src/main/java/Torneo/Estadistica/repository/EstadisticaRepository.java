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
    List<Estadistica> findByPartidaId(Long partidaId);


    // query busca una metrica especifica de un jugador en una partida
    @Query("SELECT e FROM Estadistica  e WHERE  e.usuarioId = :usuarioId AND e.usuarioId = :partidaId")
    List<Estadistica> findEstadisticaByUsuarioIdAndMetrica(@Param("usuarioId") Long usuarioId, @Param("partidaId") Long partidaId);

}
