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
        List<Estadistica> estadisticasActivas = estadisticaRepository.findAll().stream()
                .filter(Estadistica::getActivo)
                .toList();
        log.info("Hay {} registros de estadísticas activas en total", estadisticasActivas.size());
        return estadisticasActivas.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EstadisticaResponseDTO obtenerPorId(Long id) {
        log.info("buscando estadistica con ID: {}",id);
        return estadisticaRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("No se encontro ninguna estadistica con ID {}"+ id));

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
        generarAuditoria("se guardo estadistica");
        return mapToDTO(guardada);

    }


    @Transactional
    public EstadisticaResponseDTO actualizar(Long id, EstadisticaRequestDTO dto) {
        Estadistica estadistica = estadisticaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró ninguna estadística con el ID: " + id));

        usuarioClient.obtenerUsuarioPorId(dto.getUsuarioId());
        partidaClient.obtenerPartidaPorId(dto.getPartidasTorneoId());

        estadistica.setUsuarioId(dto.getUsuarioId());
        estadistica.setPartidasTorneoId(dto.getPartidasTorneoId());
        estadistica.setMetrica(dto.getMetrica());
        estadistica.setValor(dto.getValor());
        estadistica.setActivo(dto.getActivo());

        Estadistica actualizada = estadisticaRepository.save(estadistica);

        return mapToDTO(actualizada);

    }

    @Transactional
    public void eliminar(Long id){
        log.info("Procesando solicitud de borrado lógico para la estadística ID: {}", id);

        Estadistica existente = estadisticaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Intento de eliminación fallido: no se encontró estadística con el ID: " + id));
        existente.setActivo(false);
        estadisticaRepository.save(existente);

        log.info("La estadística ID: {} fue desactivada correctamente", id);
        generarAuditoria("Se eliminó estadística");
    }

    public void generarAuditoria(String detalle){
        AuditoriaRequestDTO dto = new AuditoriaRequestDTO();
        LocalDate ahora = LocalDate.now();
        dto.setDetalle(detalle);
        dto.setFecha(ahora);

        AuditoriaResponseDTO respuesta = auditoriaClient.generarAuditoria(dto);
    }

}
