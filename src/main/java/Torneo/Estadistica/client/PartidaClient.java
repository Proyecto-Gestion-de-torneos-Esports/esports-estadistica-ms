package Torneo.Estadistica.client;

import Torneo.Estadistica.dto.PartidaResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "partidas", path = "/api/partidas")
public interface PartidaClient {
    @GetMapping("/{id}")
    PartidaResponseDTO obtenerPartidaPorId(@PathVariable Long id);
}
