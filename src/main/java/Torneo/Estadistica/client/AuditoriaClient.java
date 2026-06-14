package Torneo.Estadistica.client;

import Torneo.Estadistica.dto.AuditoriaRequestDTO;
import Torneo.Estadistica.dto.AuditoriaResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "microservicio-auditoria", path = "/api/auditoria")
public interface AuditoriaClient {
    @PostMapping
    public AuditoriaResponseDTO generarAuditoria(@RequestBody AuditoriaRequestDTO auditoria);

}
