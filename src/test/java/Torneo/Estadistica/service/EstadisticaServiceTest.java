package Torneo.Estadistica.service;

import Torneo.Estadistica.client.AuditoriaClient;
import Torneo.Estadistica.client.PartidaClient;
import Torneo.Estadistica.client.UsuarioClient;
import Torneo.Estadistica.dto.EstadisticaRequestDTO;
import Torneo.Estadistica.dto.EstadisticaResponseDTO;
import Torneo.Estadistica.model.Estadistica;
import Torneo.Estadistica.repository.EstadisticaRepository;
import Torneo.Estadistica.service.EstadisticaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EstadisticaServiceTest {

    @Mock
    private EstadisticaRepository estadisticaRepository;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private PartidaClient partidaClient;

    @Mock
    private AuditoriaClient auditoriaClient;

    @InjectMocks
    private EstadisticaService estadisticaService;

    @Test
    void testObtenerTodas(){
        Estadistica estActiva = new Estadistica(1L, 5L, 10L, "Kills", 10, true);
        Estadistica estInactiva = new Estadistica(2L, 5L, 10L, "Muertes", 5, false);

        when(estadisticaRepository.findAll()).thenReturn(List.of(estActiva,estInactiva));

        List<EstadisticaResponseDTO> resultados = estadisticaService.obtenertodas();

        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals(1L, resultados.get(0).getEstadisticaId());
    }

    @Test
    void testBuscarPorId(){
        Long id = 1L;
        Estadistica estadistica = new Estadistica(id, 2L, 10L, "asistencias", 5, true);

        when(estadisticaRepository.findById(id)).thenReturn(Optional.of(estadistica));

        EstadisticaResponseDTO resultado = estadisticaService.obtenerPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getEstadisticaId());
        assertEquals("asistencias", resultado.getMetrica());

        verify(estadisticaRepository).findById(id);
    }

    @Test
    void testFindByidNotFound(){
        when(estadisticaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> estadisticaService.obtenerPorId(99L));

        verify(estadisticaRepository).findById(99L);
    }
    @Test
    void testResgistrarEstadistica(){
        EstadisticaRequestDTO requestDTO = new EstadisticaRequestDTO(5L, 10L, "Kills", 20, true);
        Estadistica estadisticaGuardada = new Estadistica(1L, 5L, 10L, "Kills", 20, true);

        when(estadisticaRepository.save(any(Estadistica.class))).thenReturn(estadisticaGuardada);
        EstadisticaResponseDTO resultado = estadisticaService.registrarEstadistica(requestDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getEstadisticaId());

        verify(usuarioClient).obtenerUsuarioPorId(5L);
        verify(partidaClient).obtenerPartidaPorId(10L);
        verify(estadisticaRepository).save(any(Estadistica.class));
        verify(auditoriaClient).generarAuditoria(any());
    }

    @Test
    void testActualizarEstadistica(){
        Long id = 1L;
        Estadistica estadisticaAntigua = new Estadistica(id, 5L, 10L, "Kills", 10, true);
        Estadistica estadisticaModificada = new Estadistica(id, 5L, 10L, "Asistencias", 15, true);

        EstadisticaRequestDTO requestDTO = new EstadisticaRequestDTO(5L, 10L, "Asistencias", 15, true);

        when(estadisticaRepository.findById(id)).thenReturn(Optional.of(estadisticaAntigua));
        when(estadisticaRepository.save(any(Estadistica.class))).thenReturn(estadisticaModificada);

        EstadisticaResponseDTO resultado = estadisticaService.actualizar(id, requestDTO);

        assertNotNull(resultado);
        assertEquals("Asistencias", resultado.getMetrica());
        assertEquals(15, resultado.getValor());

        verify(usuarioClient).obtenerUsuarioPorId(5L);
        verify(partidaClient).obtenerPartidaPorId(10L);
        verify(estadisticaRepository).findById(id);
        verify(estadisticaRepository).save(any(Estadistica.class));

    }

    @Test
    void testEliminarEstadistica(){
        Long id =1L;
        Estadistica estadistica = new Estadistica();
        estadistica.setEstadisticaId(id);
        estadistica.setActivo(true);

        when(estadisticaRepository.findById(id)).thenReturn((Optional.of(estadistica)));

        estadisticaService.eliminar(id);

        assertFalse(estadistica.getActivo());
        verify(estadisticaRepository).findById(id);
        verify(estadisticaRepository).save(estadistica);
        verify(estadisticaRepository, never()).deleteById(anyLong());
    }
}
