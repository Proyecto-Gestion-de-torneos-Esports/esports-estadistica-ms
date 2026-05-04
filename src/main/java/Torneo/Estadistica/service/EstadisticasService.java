package Torneo.Estadistica.service;

import Torneo.Estadistica.dto.EstadisticaRequestDTO;
import Torneo.Estadistica.dto.EstadisticaResponseDTO;
import Torneo.Estadistica.model.Estadistica;
import Torneo.Estadistica.repository.EstadisticaRepository;
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

    private EstadisticaResponseDTO mapToDTO(Estadistica est) {
        return new EstadisticaResponseDTO(
                est.getId(),
                est.getUsuarioId(),
                est.getPartidaId(),
                est.getMetrica(),
                est.getValor(),
                est.getActivo()
        );
    }

    @Transactional(readOnly = true)
    public List<EstadisticaResponseDTO> obtenertodas() {
        log.info("listando todas las estadisticas");
        List<Estadistica> estadisticas = estadisticaRepository.findAll();
        log.info("hay {} registros de estadisticas en total", estadisticas.size());
        return estadisticas.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<EstadisticaResponseDTO> obtenerPorId(Long id) {
        Optional<EstadisticaResponseDTO> resultado = estadisticaRepository.findById(id).map(this::mapToDTO);

        resultado.ifPresentOrElse(
                dto -> log.info("Estadisticas ID '{}' encontrada correctamente", id),
                () -> log.warn("No se encontro ninguna estadistica con el ID: {}", id)
        );
        return resultado;

    }

    @Transactional
    public EstadisticaResponseDTO guardar(EstadisticaRequestDTO dto) {
        Estadistica estadistica = new Estadistica(
                null,
                dto.getJugadorId(),
                dto.getPartidaId(),
                dto.getMetrica(),
                dto.getValor(),
                true
        );

        EstadisticaResponseDTO respuesta = mapToDTO(estadisticaRepository.save(estadistica));
        log.info("Estadistica '{}'(Valor: {}) registrada correctamente ID: {}",
                dto.getMetrica(), dto.getValor(), dto.getJugadorId());
        return respuesta;

    }

    @Transactional
    public Optional <EstadisticaResponseDTO> actualizar(Long id, EstadisticaRequestDTO dto) {
        return estadisticaRepository.findById(id).map(existente -> {
            log.info("Estadistica con ID: {} encontrada. Actualizando sus valores", id);

            existente.setUsuarioId(dto.getJugadorId());
            existente.setPartidaId(dto.getPartidaId());
            existente.setMetrica(dto.getMetrica());
            existente.setValor(dto.getValor());

            EstadisticaResponseDTO respuesta = mapToDTO((estadisticaRepository.save(existente)));
            log.info("La estadistica ID: {} fue encontrada correctamente ", id);
            return respuesta;
        });

    }

    @Transactional
    public void eliminar(Long id){
        log.info("Procesando solicitud para eliminar permannentemente la estadistica con ID {} ", id );
        estadisticaRepository.findById(id).ifPresentOrElse(existente -> {
            estadisticaRepository.delete(existente);
            log.info("la estadistica ID: {} fue eliminada correctamente de la base de datos", id);
        }, ()-> {
            log.warn("Intento de eliminacion fallido: no se encontro estadistica con el ID {}", id );
        });
    }

}
