package Torneo.Estadistica.service;

import Torneo.Estadistica.dto.EstadisticaResponseDTO;
import Torneo.Estadistica.model.Estadistica;
import Torneo.Estadistica.repository.EstadisticaRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EstadisticasService {

    private final EstadisticaRepository estadisticaRepository;

private EstadisticaResponseDTO mapToDTO(Estadistica est){
    return new EstadisticaResponseDTO(
            est.getId(),
            est.getJugadorId(),
            est.getPartidaId(),
            est.getMetrica(),
            est.getValor()
    );
}
@Transactional(readOnly = true)
public List<EstadisticaResponseDTO> obtenertodas(){
    log.info("listando todas las estadisticas");
    List<Estadistica> estadisticas = estadisticaRepository.findAll();
    log.info("hay {} registros de estadisticas en total", estadisticas.size());
    return estadisticas.stream().map(this::mapToDTO).collect(Collectors.toList());
}

@Transactional(readOnly = true)
    public Optional<EstadisticaResponseDTO> obtenerPorid(Long id){
    Optional<EstadisticaResponseDTO> resultado = estadisticaRepository.findById(id).map(this::mapToDTO);

    resultado.ifPresentOrElse(
            dto -> log.info("Estadisticas ID '{}' encontrada correctamente", id),
            () -> log.warn("No se encontro ninguna estadistica con el ID: {}", id)
    );
    return resultado;
}



}
