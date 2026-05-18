package Torneo.Estadistica.service;

import Torneo.Estadistica.client.AuditoriaClient;
import Torneo.Estadistica.client.PartidaClient;
import Torneo.Estadistica.client.UsuarioClient;
import Torneo.Estadistica.dto.*;
import Torneo.Estadistica.model.Estadistica;
import Torneo.Estadistica.repository.EstadisticaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EstadisticaService {

    private final EstadisticaRepository estadisticaRepository;
    private final AuditoriaClient auditoriaClient;
    private final PartidaClient partidaClient;
    private final UsuarioClient usuarioClient;

    private EstadisticaResponseDTO mapToDTO(Estadistica est) {
        return new EstadisticaResponseDTO(
                est.getEstadisticaId(),
                est.getUsuarioId(),
                est.getPartidasTorneoId(),
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
    public EstadisticaResponseDTO registrarEstadistica(EstadisticaRequestDTO dto) {
        log.info("Registrando métrica '{}' para el usuario ID: {} en la partida ID: {}",
                dto.getMetrica(), dto.getUsuarioId(), dto.getPartidasTorneoId());

        //  Validación
        usuarioClient.obtenerUsuarioPorId(dto.getUsuarioId());
        partidaClient.obtenerPartidaPorId(dto.getPartidasTorneoId());

        // map
        Estadistica estadistica = new Estadistica();

        estadistica.setUsuarioId(dto.getUsuarioId());
        estadistica.setPartidasTorneoId(dto.getPartidasTorneoId());
        estadistica.setMetrica(dto.getMetrica());
        estadistica.setValor(dto.getValor());
        estadistica.setActivo(true);

        // Guardado
        Estadistica guardada = estadisticaRepository.save(estadistica);
        log.info("Estadística guardada correctamente con ID: {}", guardada.getEstadisticaId());

        return mapToDTO(guardada);

    }


    @Transactional
    public Optional <EstadisticaResponseDTO> actualizar(Long id, EstadisticaRequestDTO dto) {
        return estadisticaRepository.findById(id).map(existente -> {
            log.info("Estadistica con ID: {} encontrada. Actualizando sus valores", id);

            existente.setUsuarioId(dto.getUsuarioId());
            existente.setPartidasTorneoId(dto.getPartidasTorneoId());
            existente.setMetrica(dto.getMetrica());
            existente.setValor(dto.getValor());

            EstadisticaResponseDTO respuesta = mapToDTO((estadisticaRepository.save(existente)));
            log.info("La estadistica ID: {} fue encontrada correctamente ", id);
            generarAuditoria("Se actualizo estadistica");
            return respuesta;
        });

    }

    @Transactional
    public void eliminar(Long id){
        log.info("Procesando solicitud para eliminar permanentemente la estadistica con ID {} ", id );
        estadisticaRepository.findById(id).ifPresentOrElse(existente -> {
            estadisticaRepository.delete(existente);
            log.info("la estadistica ID: {} fue eliminada correctamente de la base de datos", id);
            generarAuditoria("Se elimino ID estadistica");
        }, ()-> {
            log.warn("Intento de eliminacion fallido: no se encontro estadistica con el ID {}", id );
        });
    }

    public void generarAuditoria(String detalle){
        AuditoriaRequestDTO dto = new AuditoriaRequestDTO();
        LocalDate ahora = LocalDate.now();
        dto.setDetalle(detalle);
        dto.setFecha(ahora);

        AuditoriaResponseDTO respuesta = auditoriaClient.generarAuditoria(dto);
    }

}
