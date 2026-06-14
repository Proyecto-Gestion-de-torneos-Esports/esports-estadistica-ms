package Torneo.Estadistica.client;

import Torneo.Estadistica.dto.UsuarioResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuarios", path = "/api/usuarios")
public interface UsuarioClient {
    @GetMapping("/{id}")
    UsuarioResponseDTO obtenerUsuarioPorId(@PathVariable  Long id);

}
